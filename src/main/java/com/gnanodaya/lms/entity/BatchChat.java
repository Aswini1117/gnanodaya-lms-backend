package com.gnanodaya.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "batch_chats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    private String messageType;

    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    private Boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime sentAt;
}