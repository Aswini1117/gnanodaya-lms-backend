package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    List<CalendarEvent> findByInstituteId(Long instituteId);
    List<CalendarEvent> findByBatchId(Long batchId);
    List<CalendarEvent> findByCreatedById(Long userId);
    List<CalendarEvent> findByInstituteIdAndStartDateTimeBetween(
            Long instituteId, LocalDateTime start, LocalDateTime end);
    List<CalendarEvent> findByBatchIdAndStartDateTimeBetween(
            Long batchId, LocalDateTime start, LocalDateTime end);
}