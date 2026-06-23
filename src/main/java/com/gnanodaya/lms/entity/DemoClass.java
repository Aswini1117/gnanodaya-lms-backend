package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "demo_classes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemoClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentName;

    private String phone;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    private LocalDateTime scheduledAt;

    // SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
    private String status;

    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;
}