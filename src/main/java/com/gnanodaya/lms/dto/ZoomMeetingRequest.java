package com.gnanodaya.lms.dto.zoom;

import com.gnanodaya.lms.enums.ZoomTargetType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ZoomMeetingRequest {

    private String topic;
    private String agenda;
    private LocalDateTime startTime;
    private Integer duration;

    private Long batchId;

    private ZoomTargetType targetType;

    private List<Long> invitedUserIds;

    private Long instituteId;
}