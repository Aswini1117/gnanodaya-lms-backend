package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.CalendarEvent;
import com.gnanodaya.lms.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CalendarEvent>> createEvent(@RequestBody CalendarEvent event) {
        return ResponseEntity.ok(ApiResponse.success("Event created", calendarService.createEvent(event)));
    }

    // Institute-wide events (for Admin view)
    @GetMapping("/institute/{instituteId}")
    public ResponseEntity<ApiResponse<List<CalendarEvent>>> getInstituteEvents(@PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Events", calendarService.getEventsByInstitute(instituteId)));
    }

    // Batch-specific events (for Student + Instructor view)
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<ApiResponse<List<CalendarEvent>>> getBatchEvents(@PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Batch events", calendarService.getEventsByBatch(batchId)));
    }

    // Personal events by creator
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<CalendarEvent>>> getUserEvents(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("My events", calendarService.getEventsByUser(userId)));
    }

    // Events for a specific month range
    @GetMapping("/institute/{instituteId}/range")
    public ResponseEntity<ApiResponse<List<CalendarEvent>>> getEventsInRange(
            @PathVariable Long instituteId,
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(ApiResponse.success("Events in range",
                calendarService.getEventsForMonth(instituteId,
                        LocalDateTime.parse(start), LocalDateTime.parse(end))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CalendarEvent>> updateEvent(
            @PathVariable Long id, @RequestBody CalendarEvent event) {
        return ResponseEntity.ok(ApiResponse.success("Event updated", calendarService.updateEvent(id, event)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long id) {
        calendarService.deleteEvent(id);
        return ResponseEntity.ok(ApiResponse.success("Event deleted", null));
    }
}