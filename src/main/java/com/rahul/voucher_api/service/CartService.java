package com.rahul.voucher_api.service;

import com.rahul.voucher_api.entity.Cart;
import com.rahul.voucher_api.entity.CartItem;

import java.util.List;
import java.util.UUID;

public interface CartService {
    Cart createCart(UUID userId);
    Cart getCartById(UUID cartId);
    void deleteCart(UUID cartId);
    List<CartItem> getCartItems(UUID cartId);
    Cart addCartItem(UUID cartId, UUID productId, int quantity);
    void removeCartItem(UUID cartId, UUID cartItemId, int quantityToRemove);}