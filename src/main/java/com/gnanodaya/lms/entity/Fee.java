package com.gnanodaya.lms.entity;

import com.gnanodaya.lms.enums.FeeStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal pendingAmount;

    @Enumerated(EnumType.STRING)
    private FeeStatus status;

    private LocalDate dueDate;
    private LocalDate paidDate;
    private String paymentMode; // CASH, ONLINE, UPI, CARD
    private String transactionId;
    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}