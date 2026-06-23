package com.gnanodaya.lms.entity;

import com.gnanodaya.lms.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String thumbnail;

    private BigDecimal fees;

    private Integer durationWeeks;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Batch> batches;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}