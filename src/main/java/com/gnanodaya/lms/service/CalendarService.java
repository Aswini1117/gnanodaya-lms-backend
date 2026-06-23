package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.CalendarEvent;
import com.gnanodaya.lms.repository.CalendarEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarEventRepository calendarEventRepository;

    public CalendarEvent createEvent(CalendarEvent event) {
        return calendarEventRepository.save(event);
    }

    public List<CalendarEvent> getEventsByInstitute(Long instituteId) {
        return calendarEventRepository.findByInstituteId(instituteId);
    }

    public List<CalendarEvent> getEventsByBatch(Long batchId) {
        return calendarEventRepository.findByBatchId(batchId);
    }

    public List<CalendarEvent> getEventsByUser(Long userId) {
        return calendarEventRepository.findByCreatedById(userId);
    }

    public List<CalendarEvent> getEventsForMonth(Long instituteId, LocalDateTime start, LocalDateTime end) {
        return calendarEventRepository.findByInstituteIdAndStartDateTimeBetween(instituteId, start, end);
    }

    public CalendarEvent updateEvent(Long id, CalendarEvent updated) {
        CalendarEvent event = calendarEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setTitle(updated.getTitle());
        event.setDescription(updated.getDescription());
        event.setStartDateTime(updated.getStartDateTime());
        event.setEndDateTime(updated.getEndDateTime());
        event.setEventType(updated.getEventType());
        event.setColor(updated.getColor());
        return calendarEventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        calendarEventRepository.deleteById(id);
    }
}