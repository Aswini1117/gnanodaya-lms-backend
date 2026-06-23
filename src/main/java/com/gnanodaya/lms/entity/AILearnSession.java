package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_learn_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AILearnSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private String sessionType;
    private String topic;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String aiResponse;

    private String status;
    private Integer durationMinutes;
    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @CreationTimestamp
    private LocalDateTime createdAt;
}