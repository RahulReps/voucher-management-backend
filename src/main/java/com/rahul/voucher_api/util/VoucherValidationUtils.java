package com.rahul.voucher_api.util;

import com.rahul.voucher_api.entity.Voucher;
import org.springframework.util.StringUtils;

public class VoucherValidationUtils {

    public static void validateVoucher(Voucher voucher) {
        if (voucher == null) throw new IllegalArgumentException("Voucher cannot be null");
        if (!StringUtils.hasText(voucher.getCode())) throw new IllegalArgumentException("Voucher code cannot be empty");
        if (voucher.getType() == null) throw new IllegalArgumentException("Voucher type cannot be null");

        switch (voucher.getType()) {
            case PERCENTAGE_DISCOUNT:
                validatePercentageDiscount(voucher);
                break;
            case FIXED_AMOUNT:
                validateFixedAmount(voucher);
                break;
            case BUY_ONE_GET_ONE:
            case FREE_ITEM:
                validateProductSpecificVoucher(voucher);
                break;
            default:
                throw new IllegalArgumentException("Unsupported voucher type");
        }
    }

    private static void validatePercentageDiscount(Voucher voucher) {
        if (voucher.getValue() == null || voucher.getValue() <= 0) {
            throw new IllegalArgumentException("Percentage discount value must be greater than 0");
        }
        if (voucher.getCapAmount() != null && voucher.getCapAmount() <= 0) {
            throw new IllegalArgumentException("Cap amount must be greater than 0 if specified");
        }
        if(voucher.getValue() > 100){
            throw new IllegalArgumentException("Percentage discount value must be less than or equal to 100");
        }
    }

    private static void validateFixedAmount(Voucher voucher) {
        if (voucher.getValue() == null || voucher.getValue() <= 0) {
            throw new IllegalArgumentException("Fixed amount value must be greater than 0");
        }
    }

    private static void validateProductSpecificVoucher(Voucher voucher) {
        if (voucher.getApplicableProductIds() == null || voucher.getApplicableProductIds().isEmpty()) {
            throw new IllegalArgumentException("Applicable product IDs must be specified");
        }
    }
}
