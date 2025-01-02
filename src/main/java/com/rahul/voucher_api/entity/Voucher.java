package com.rahul.voucher_api.entity;

import com.rahul.voucher_api.enums.VoucherType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "vouchers")
public class Voucher {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoucherType type;

    @Column(nullable = false)
    private Double value;

    private Double capAmount;

    private Double minimumCartAmount;

    @Column(nullable = false)
    private Integer maxUsagePerUser;

    @Column(nullable = false)
    private Integer maxTotalUsage;

    @Column(nullable = false)
    private Integer currentUsageCount;

    private Integer requiredOrderCount;

    private Integer maximumOrderCount;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "voucher_user_groups",
            joinColumns = @JoinColumn(name = "voucher_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> applicableUserGroups;

    @ManyToMany
    @JoinTable(
            name = "voucher_product_ids",
            joinColumns = @JoinColumn(name = "voucher_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> applicableProductIds;
}