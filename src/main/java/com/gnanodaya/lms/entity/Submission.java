package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    private String fileUrl;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private Integer marksObtained;

    private String feedback;

    private Boolean isGraded;

    @CreationTimestamp
    private LocalDateTime submittedAt;
}