package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "self_paced_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelfPacedCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String thumbnail;
    private BigDecimal price;
    private Integer totalVideos;
    private Integer totalDurationMinutes;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "selfPacedCourse", cascade = CascadeType.ALL)
    private List<SelfPacedEnrollment> enrollments;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
