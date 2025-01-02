package com.rahul.voucher_api.service;

import com.rahul.voucher_api.entity.Order;

import java.util.UUID;

public interface OrderService {
    Order placeOrder(UUID userId, UUID cartId);
}