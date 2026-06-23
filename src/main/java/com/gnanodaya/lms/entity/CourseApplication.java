package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    private String status;
    private String remarks;

    @CreationTimestamp
    private LocalDateTime appliedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}