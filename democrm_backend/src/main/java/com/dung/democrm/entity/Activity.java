package com.dung.democrm.entity;

import com.dung.democrm.common.enums.ActivityStatus;
import com.dung.democrm.common.enums.ActivityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
public class Activity extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityStatus status = ActivityStatus.PENDING;

    @Column(nullable = false, length = 150)
    private String subject;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDateTime completedAt;
}
