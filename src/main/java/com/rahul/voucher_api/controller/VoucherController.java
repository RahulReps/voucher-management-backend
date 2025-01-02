package com.rahul.voucher_api.controller;

import com.rahul.voucher_api.entity.Cart;
import com.rahul.voucher_api.entity.Voucher;
import com.rahul.voucher_api.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @PostMapping
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucher) {
        Voucher createdVoucher = voucherService.createVoucher(voucher);
        return ResponseEntity.ok(createdVoucher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable UUID id, @RequestBody Voucher voucher) {
        voucher.setId(id);
        Voucher updatedVoucher = voucherService.createVoucher(voucher);
        return ResponseEntity.ok(updatedVoucher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable UUID id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Voucher>> getAllVouchers() {
        List<Voucher> vouchers = voucherService.getAllVouchers();
        return ResponseEntity.ok(vouchers);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Voucher> getVoucherByCode(@PathVariable UUID code) {
        Voucher voucher = voucherService.getVoucherByCode(code);
        return ResponseEntity.ok(voucher);
    }

    @PostMapping("/apply")
    public ResponseEntity<Cart> applyVoucher(@RequestParam String code, @RequestBody Cart cart) {
        Cart updatedCart = voucherService.applyVoucher(code, cart);
        return ResponseEntity.ok(updatedCart);
    }
}