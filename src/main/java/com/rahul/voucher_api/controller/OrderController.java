package com.rahul.voucher_api.controller;

import com.rahul.voucher_api.entity.Order;
import com.rahul.voucher_api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestParam UUID userId, @RequestParam UUID cartId) {
        Order order = orderService.placeOrder(userId, cartId);
        return ResponseEntity.ok(order);
    }
}