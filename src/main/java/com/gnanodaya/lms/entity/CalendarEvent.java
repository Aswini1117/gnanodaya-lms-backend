package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String eventType;

    private String targetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    private String color;

    @CreationTimestamp
    private LocalDateTime createdAt;
}