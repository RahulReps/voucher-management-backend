package com.rahul.voucher_api.dto;

import com.rahul.voucher_api.enums.VoucherType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VoucherDTO {
    private String code;
    private VoucherType type;
    private Double value;
    private Double capAmount;
    private Double minimumCartAmount;
    private Integer maxUsagePerUser;
    private Integer maxTotalUsage;
    private Integer currentUsageCount;
    private Integer requiredOrderCount;
    private Integer maximumOrderCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
    private List<String> applicableProductIds; // Only accept product IDs in the request
}
