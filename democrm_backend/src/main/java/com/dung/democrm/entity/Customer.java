package com.dung.democrm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "phone"))
@Getter
@Setter
@NoArgsConstructor
public class Customer extends BaseEntity{

    @Column(nullable = false)
    private String name;

    private String company;

    @Column(nullable = false)
    private String phone;

    @Column(unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;
}
