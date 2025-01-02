package com.rahul.voucher_api.service;

import com.rahul.voucher_api.entity.*;
import com.rahul.voucher_api.enums.OrderStatus;
import com.rahul.voucher_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Order placeOrder(UUID userId, UUID cartId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setSubtotal(cart.getSubtotal());
        order.setDiscount(0.0);
        order.setTotal(cart.getSubtotal());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = cartItemRepository.findByCart(cart).stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setTotal(cartItem.getTotal());
            return orderItem;
        }).collect(Collectors.toList());

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        // Clear the cart items
        cartItemRepository.deleteAll(cartItemRepository.findByCart(cart));
        cart.setSubtotal(0.0);
        cartRepository.save(cart);

        return order;
    }
}