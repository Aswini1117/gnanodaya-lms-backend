package com.gnanodaya.lms.dto.zoom;

import com.gnanodaya.lms.enums.ZoomMeetingStatus;
import com.gnanodaya.lms.enums.ZoomTargetType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ZoomMeetingResponse {
    private Long id;
    private String zoomMeetingId;
    private String topic;
    private String agenda;
    private LocalDateTime startTime;
    private Integer duration;
    private String joinUrl;
    private String startUrl;
    private String meetingPassword;
    private ZoomMeetingStatus status;
    private String instructorName;
    private String batchName;
    private String courseName;
    private ZoomTargetType targetType;
    private List<String> inviteeNames;
}