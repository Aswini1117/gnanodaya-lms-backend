package com.gnanodaya.lms.entity;

import com.gnanodaya.lms.enums.ZoomMeetingStatus;
import com.gnanodaya.lms.enums.ZoomTargetType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "zoom_meetings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoomMeeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String zoomMeetingId;

    @Column(nullable = false)
    private String topic;

    @Column(columnDefinition = "TEXT")
    private String agenda;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private Integer duration; // in minutes

    @Column(columnDefinition = "TEXT")
    private String joinUrl;

    @Column(columnDefinition = "TEXT")
    private String startUrl;

    private String meetingPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ZoomMeetingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ZoomTargetType targetType = ZoomTargetType.BOTH;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "zoom_meeting_invitees",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private Set<User> invitees = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}