package com.gnanodaya.lms.dto.announcement;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnnouncementResponse {
    private Long id;
    private String title;
    private String content;
    private String targetType;
    private LocalDateTime createdAt;
}