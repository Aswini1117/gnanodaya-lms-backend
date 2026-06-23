package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "mock_interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockInterview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    private String topic;

    private Integer totalQuestions;

    private Integer score;

    private Integer durationMinutes;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    // COMPLETED, IN_PROGRESS, PENDING
    private String status;

    @CreationTimestamp
    private LocalDateTime attemptedAt;
}