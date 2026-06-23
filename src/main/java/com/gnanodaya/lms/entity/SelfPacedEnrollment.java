package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "self_paced_enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelfPacedEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "self_paced_course_id", nullable = false)
    private SelfPacedCourse selfPacedCourse;

    private Integer progressPercent;
    private String status;

    @CreationTimestamp
    private LocalDateTime enrolledAt;
}