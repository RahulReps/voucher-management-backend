package com.rahul.voucher_api.repository;

import com.rahul.voucher_api.entity.Cart;
import com.rahul.voucher_api.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByCart(Cart cart);
    List<CartItem> findByCartId(UUID cartId);
    CartItem findByCartIdAndProductId(UUID cartId, UUID productId);
}