package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private CourseContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    private Integer watchedDuration;
    private Integer totalDuration;
    private Boolean isCompleted;
    private Integer progressPercent;

    @CreationTimestamp
    private LocalDateTime startedAt;

    @UpdateTimestamp
    private LocalDateTime lastWatchedAt;
}