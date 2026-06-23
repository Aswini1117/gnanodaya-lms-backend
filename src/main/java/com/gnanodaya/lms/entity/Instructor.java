package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "instructor_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String qualification;
    private String experience;
    private String skills;
    private String linkedIn;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String specialization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}