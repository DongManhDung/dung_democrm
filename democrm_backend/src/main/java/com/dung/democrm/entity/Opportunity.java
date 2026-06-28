package com.dung.democrm.entity;

import com.dung.democrm.common.enums.OpportunityStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "opportunities")
@Getter
@Setter
@NoArgsConstructor
public class Opportunity extends BaseEntity{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private Lead lead;

    private BigDecimal amount;

    private Integer probability;

    @Enumerated(EnumType.STRING)
    private OpportunityStatus status;
}
