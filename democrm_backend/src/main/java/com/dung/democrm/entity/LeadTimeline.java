package com.dung.democrm.entity;

import com.dung.democrm.common.enums.TimelineAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lead_timelines")
@Getter
@Setter
@NoArgsConstructor
public class LeadTimeline extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TimelineAction action;

    @Column(nullable = false, length = 700)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by", nullable = false)
    private User performedBy;
}
