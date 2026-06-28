package com.dung.democrm.entity;

import com.dung.democrm.common.enums.LeadSource;
import com.dung.democrm.common.enums.LeadStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leads")
@Getter
@Setter
@NoArgsConstructor
public class Lead extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Enumerated(EnumType.STRING)
    private LeadStatus leadStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeadSource source;

    private LocalDate assignedAt;

    private LocalDate expiredAt;
}
