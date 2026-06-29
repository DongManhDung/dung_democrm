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
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Sales đang chăm sóc lead
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Manager quản lý team của lead, không đổi khi chuyển Lead giữa các Sales cùng team
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_owner_id", nullable = false)
    private User teamOwner;

    // Tự đặt là mới tạo khi có 1 lead mới
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeadStatus leadStatus = LeadStatus.NEW;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeadSource source;

    // Khi sales nhận đuợc lead
    @Column(nullable = false)
    private LocalDate assignedAt;

    // Sau ngày này sales khác mới được xin
    @Column(nullable = false)
    private LocalDate expiredAt;

    @Column(length = 300)
    private String lostReason;
}
