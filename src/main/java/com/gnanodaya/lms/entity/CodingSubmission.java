package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "coding_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodingSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private CodingQuestion question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String code;

    private String language;
    private String verdict;
    private Integer executionTimeMs;
    private Integer memoryUsedKb;

    @Column(columnDefinition = "TEXT")
    private String output;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @CreationTimestamp
    private LocalDateTime submittedAt;
}