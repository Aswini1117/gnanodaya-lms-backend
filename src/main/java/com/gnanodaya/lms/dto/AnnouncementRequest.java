package com.gnanodaya.lms.dto.announcement;

import lombok.Data;

@Data
public class AnnouncementRequest {
    private String title;
    private String content;
    private String targetType; // ALL, STUDENTS, INSTRUCTORS, BATCH
    private Long instituteId;
    private Long createdById;
    private Long batchId; // optional, only when targetType = BATCH
}