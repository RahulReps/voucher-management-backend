package com.rahul.voucher_api.service;

import com.rahul.voucher_api.entity.Cart;
import com.rahul.voucher_api.entity.Voucher;

import java.util.List;
import java.util.UUID;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);
    Voucher getVoucherByCode(UUID code);
    List<Voucher> getAllVouchers();
    void deleteVoucher(UUID id);
    Cart applyVoucher(String code, Cart cart);
}