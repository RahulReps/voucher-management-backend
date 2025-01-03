package com.rahul.voucher_api.repository;

import com.rahul.voucher_api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}