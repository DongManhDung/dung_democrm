package com.dung.democrm.entity;

import com.dung.democrm.common.enums.LeadPriority;
import com.dung.democrm.common.enums.LeadSource;
import com.dung.democrm.common.enums.LeadStatus;
import com.dung.democrm.common.enums.LostReason;
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

    // Manager của Sales (giữ nguyên khi transfer trong cùng team)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeadPriority priority = LeadPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private LostReason lostReason;

    @Column(length = 500)
    private String note;

    // Khi sales nhận đuợc lead
    @Column(nullable = false)
    private LocalDate assignedAt;

    // Sau ngày này sales khác mới được xin
    @Column(nullable = false)
    private LocalDate expiredAt;

    @Column(nullable = false)
    private Integer transferCount = 0;

}
