package com.gnanodaya.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "institutes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Institute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String email;

    @Column(unique = true)
    private String phone;

    private String adminName;

    private String address;

    private String logo;

    private String website;

    private String plan;

    private String status;

    @Column(nullable = false)
    private Boolean isActive = true;

    private LocalDateTime trialEndDate;
    private String subscriptionPlan;

    @JsonIgnore
    @OneToMany(mappedBy = "institute", cascade = CascadeType.ALL)
    private List<User> users;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}