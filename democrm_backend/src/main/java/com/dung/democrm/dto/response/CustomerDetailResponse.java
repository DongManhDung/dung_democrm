package com.dung.democrm.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDetailResponse {
    private Long id;
    private String name;
    private String company;
    private String phone;
    private String email;

    private Long ownerId;
    private String ownerName;
    private String ownerEmployeeCode;

    private Integer totalLeads;
    private Integer totalOpportunities;
    private Integer totalWonOpportunities;
    private Integer totalContracts;
    private BigDecimal totalRevenue;
    private Integer totalActivities;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
