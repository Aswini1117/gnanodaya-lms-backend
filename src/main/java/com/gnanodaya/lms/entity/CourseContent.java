package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_contents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // VIDEO, PDF, DOCUMENT, LINK
    private String contentType;

    private String fileUrl;

    private String description;

    private Integer orderIndex;

    private Boolean isVisible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
}