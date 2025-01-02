package com.rahul.voucher_api.service;

import com.rahul.voucher_api.entity.CartItem;
import com.rahul.voucher_api.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartItem getCartItemById(UUID cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("CartItem not found"));
    }
}