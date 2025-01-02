package com.rahul.voucher_api.service;

import com.rahul.voucher_api.entity.CartItem;

import java.util.UUID;

public interface CartItemService {
    CartItem getCartItemById(UUID cartItemId);
}
