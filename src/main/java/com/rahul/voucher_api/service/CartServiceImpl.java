package com.rahul.voucher_api.service;

import com.rahul.voucher_api.entity.Cart;
import com.rahul.voucher_api.entity.CartItem;
import com.rahul.voucher_api.entity.Product;
import com.rahul.voucher_api.repository.CartItemRepository;
import com.rahul.voucher_api.repository.CartRepository;
import com.rahul.voucher_api.repository.ProductRepository;
import com.rahul.voucher_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart createCart(UUID userId) {
        Cart cart = new Cart();
        cart.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        cart.setSubtotal(0.0);
        cart.setCreatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(UUID cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public void deleteCart(UUID cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public List<CartItem> getCartItems(UUID cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public Cart addCartItem(UUID cartId, UUID productId, int quantity) {
        Cart cart = getCartById(cartId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem existingCartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            existingCartItem.setTotal(existingCartItem.getPrice() * existingCartItem.getQuantity());
            cart.setSubtotal(cart.getSubtotal() + (product.getPrice() * quantity));
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice());
            cartItem.setTotal(product.getPrice() * quantity);

            cart.setSubtotal(cart.getSubtotal() + cartItem.getTotal());
            cartItemRepository.save(cartItem);
        }

        return cartRepository.save(cart);
    }

    @Override
    public void removeCartItem(UUID cartId, UUID cartItemId, int quantityToRemove) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("CartItem not found"));
        Cart cart = cartItem.getCart();

        if (cartItem.getQuantity() > quantityToRemove) {
            cartItem.setQuantity(cartItem.getQuantity() - quantityToRemove);
            cartItem.setTotal(cartItem.getPrice() * cartItem.getQuantity());
            cart.setSubtotal(cart.getSubtotal() - (cartItem.getPrice() * quantityToRemove));
            cartItemRepository.save(cartItem);
        } else {
            cart.setSubtotal(cart.getSubtotal() - cartItem.getTotal());
            cartItemRepository.deleteById(cartItemId);
        }

        cartRepository.save(cart);
    }
}
