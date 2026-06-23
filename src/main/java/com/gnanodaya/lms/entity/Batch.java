package com.gnanodaya.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnanodaya.lms.enums.BatchStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "batches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String batchName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    @JsonIgnore
    private User instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id")
    @JsonIgnore
    private Institute institute;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime classTime;
    private String classDays;

    @Enumerated(EnumType.STRING)
    private BatchStatus status;

    private Integer maxStudents;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Enrollment> enrollments;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}