package com.rahul.voucher_api.controller;

import com.rahul.voucher_api.entity.Cart;
import com.rahul.voucher_api.entity.CartItem;
import com.rahul.voucher_api.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestParam UUID userId) {
        Cart cart = cartService.createCart(userId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable UUID cartId) {
        Cart cart = cartService.getCartById(cartId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable UUID cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable UUID cartId) {
        List<CartItem> cartItems = cartService.getCartItems(cartId);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<Cart> addCartItem(@PathVariable UUID cartId, @RequestParam UUID productId, @RequestParam int quantity) {
        Cart updatedCart = cartService.addCartItem(cartId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable UUID cartId, @PathVariable UUID cartItemId, @RequestParam int quantityToRemove) {
        cartService.removeCartItem(cartId, cartItemId, quantityToRemove);
        return ResponseEntity.noContent().build();
    }
}
