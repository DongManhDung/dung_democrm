package com.dung.democrm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@NoArgsConstructor
public class Contract extends BaseEntity{

    @OneToOne(fetch = FetchType.LAZY)
    private Opportunity opportunity;

    @Column(unique = true)
    private String contractNumber;

    private BigDecimal amount;

}
