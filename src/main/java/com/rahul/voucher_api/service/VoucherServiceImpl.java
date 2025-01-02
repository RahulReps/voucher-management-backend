package com.rahul.voucher_api.service;

import com.rahul.voucher_api.entity.*;
import com.rahul.voucher_api.repository.*;
import com.rahul.voucher_api.util.VoucherValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private VoucherUsageRepository voucherUsageRepository;

    @Override
    public Voucher createVoucher(Voucher voucher) {
        VoucherValidationUtils.validateVoucher(voucher);
        voucher.setCurrentUsageCount(0);
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher getVoucherByCode(UUID code) {
        return voucherRepository.findById(code).orElse(null);
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    @Override
    public void deleteVoucher(UUID id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public Cart applyVoucher(String code, Cart cart) {
        validateVoucherApplication(code, cart);
        Voucher voucher = voucherRepository.findByCode(code);
        applyVoucherLogic(voucher, cart);
        voucher.setCurrentUsageCount(voucher.getCurrentUsageCount() + 1);
        voucherRepository.save(voucher);
        updateVoucherUsage(voucher, cart.getUser(), cart.getSubtotal());
        return cart;
    }

    private void validateVoucherApplication(String code, Cart cart) {
        if (!StringUtils.hasText(code)) throw new IllegalArgumentException("Voucher code cannot be empty");
        if (cart == null) throw new IllegalArgumentException("Cart cannot be null");

        Voucher voucher = voucherRepository.findByCode(code);
        if (voucher == null || !voucher.getIsActive())
            throw new IllegalArgumentException("Invalid or inactive voucher code");

        long usageCount = voucherUsageRepository.countByUserIdAndVoucherId(cart.getUser().getId(), voucher.getId());
        if (usageCount >= voucher.getMaxUsagePerUser())
            throw new IllegalArgumentException("Voucher usage limit reached for this user");
    }

    private void updateVoucherUsage(Voucher voucher, User user, Double discountAmount) {
        VoucherUsage voucherUsage = new VoucherUsage();
        voucherUsage.setUser(user);
        voucherUsage.setVoucher(voucher);
        voucherUsage.setUsedAt(LocalDateTime.now());
        voucherUsage.setDiscountAmount(discountAmount);
        voucherUsageRepository.save(voucherUsage);
    }

    private void applyVoucherLogic(Voucher voucher, Cart cart) {
        User user = userRepository.findById(cart.getUser().getId()).orElse(null);
        cart.setUser(user);
        if (user == null) throw new IllegalArgumentException("User not found in cart");
        validateCartAndUser(voucher, cart, user);

        switch (voucher.getType()) {
            case PERCENTAGE_DISCOUNT:
                applyPercentageDiscount(voucher, cart);
                break;
            case FIXED_AMOUNT:
                applyFixedAmount(voucher, cart);
                break;
            case BUY_ONE_GET_ONE:
                applyBOGO(voucher, cart);
                break;
            case FREE_ITEM:
                applyFreeItem(voucher, cart);
                break;
            default:
                throw new IllegalArgumentException("Unsupported voucher type");
        }
    }

    private void validateCartAndUser(Voucher voucher, Cart cart, User user) {
        if (voucher.getRequiredOrderCount() != null && user.getOrderCount() < voucher.getRequiredOrderCount())
            throw new IllegalArgumentException("User does not meet required order count");

        if (voucher.getMaximumOrderCount() != null && user.getOrderCount() > voucher.getMaximumOrderCount())
            throw new IllegalArgumentException("User exceeds maximum order count");

        if (voucher.getMinimumCartAmount() != null && cart.getSubtotal() < voucher.getMinimumCartAmount())
            throw new IllegalArgumentException("Cart subtotal does not meet minimum required amount");
    }

    private void applyPercentageDiscount(Voucher voucher, Cart cart) {
        double discount = cart.getSubtotal() * (voucher.getValue() / 100);
        if (voucher.getCapAmount() != null) discount = Math.min(discount, voucher.getCapAmount());
        cart.setSubtotal(cart.getSubtotal() - discount);
    }

    private void applyFixedAmount(Voucher voucher, Cart cart) {
        if (cart.getSubtotal() < voucher.getValue())
            throw new IllegalArgumentException("Cart subtotal is less than fixed amount value");
        cart.setSubtotal(cart.getSubtotal() - voucher.getValue());
    }

    private void applyBOGO(Voucher voucher, Cart cart) {
        Product freeProduct = voucher.getApplicableProductIds().getFirst();
        if (freeProduct == null) throw new IllegalArgumentException("Free product not found");
        addFreeCartItem(cart, freeProduct);
    }

    private void applyFreeItem(Voucher voucher, Cart cart) {
        Product freeItem = voucher.getApplicableProductIds().getFirst();
        if (freeItem == null) throw new IllegalArgumentException("Free item not found");
        addFreeCartItem(cart, freeItem);
    }

    private void addFreeCartItem(Cart cart, Product freeProduct) {
        CartItem freeCartItem = new CartItem();
        freeCartItem.setCart(cart);
        freeCartItem.setProduct(freeProduct);
        freeCartItem.setQuantity(1);
        freeCartItem.setPrice(0.0);
        freeCartItem.setTotal(0.0);
        cartItemRepository.save(freeCartItem);
    }
}