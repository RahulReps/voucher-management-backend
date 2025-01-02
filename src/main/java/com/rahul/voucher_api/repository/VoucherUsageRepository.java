package com.rahul.voucher_api.repository;

import com.rahul.voucher_api.entity.VoucherUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VoucherUsageRepository extends JpaRepository<VoucherUsage, UUID> {
    VoucherUsage findByUserIdAndVoucherId(UUID userId, UUID voucherId);
    long countByUserIdAndVoucherId(UUID userId, UUID voucherId); // Add this method
}