package com.dung.democrm.entity;

import com.dung.democrm.common.enums.ActivityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
public class Activity extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private ActivityType type;

    private String note;

    private LocalDateTime activityDate;

}
