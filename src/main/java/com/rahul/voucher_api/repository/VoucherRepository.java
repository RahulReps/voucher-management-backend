package com.rahul.voucher_api.repository;

import com.rahul.voucher_api.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VoucherRepository extends JpaRepository<Voucher, UUID> {
    Voucher findByCode(String code);
}
