package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.ZoomMeeting;
import com.gnanodaya.lms.enums.ZoomMeetingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoomMeetingRepository extends JpaRepository<ZoomMeeting, Long> {

    List<ZoomMeeting> findByInstructorIdOrderByStartTimeDesc(Long instructorId);

    List<ZoomMeeting> findByBatchIdOrderByStartTimeDesc(Long batchId);

    List<ZoomMeeting> findByInstituteIdOrderByStartTimeDesc(Long instituteId);

    List<ZoomMeeting> findByBatchIdAndStatus(Long batchId, ZoomMeetingStatus status);

    List<ZoomMeeting> findByInstructorIdAndStatus(Long instructorId, ZoomMeetingStatus status);

    List<ZoomMeeting> findByInstituteIdAndStatus(Long instituteId, ZoomMeetingStatus status);

    List<ZoomMeeting> findByInviteesIdOrderByStartTimeDesc(Long userId);
}
