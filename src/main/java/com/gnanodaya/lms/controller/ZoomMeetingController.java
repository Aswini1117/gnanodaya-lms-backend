package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.dto.zoom.ZoomMeetingRequest;
import com.gnanodaya.lms.dto.zoom.ZoomMeetingResponse;
import com.gnanodaya.lms.enums.ZoomMeetingStatus;
import com.gnanodaya.lms.security.JwtUtil;
import com.gnanodaya.lms.service.ZoomMeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/zoom")
@RequiredArgsConstructor
public class ZoomMeetingController {

    private final ZoomMeetingService zoomMeetingService;
    private final JwtUtil jwtUtil;

    // ── INSTRUCTOR — Create Meeting ───────────────────
    @PostMapping("/meetings")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ZoomMeetingResponse>> createMeeting(
            @RequestBody ZoomMeetingRequest request,
            HttpServletRequest httpRequest) {

        Long userId = extractUserId(httpRequest);
        return ResponseEntity.ok(ApiResponse.success(
                "Meeting created successfully",
                zoomMeetingService.createMeeting(request, userId)));
    }

    // ── INSTRUCTOR — Get My Meetings ──────────────────
    @GetMapping("/meetings/my")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ZoomMeetingResponse>>> getMyMeetings(
            HttpServletRequest httpRequest) {

        Long userId = extractUserId(httpRequest);
        return ResponseEntity.ok(ApiResponse.success(
                "Meetings fetched",
                zoomMeetingService.getMeetingsForUser(userId)));
    }

    // ── STUDENT — Get Meetings by Batch ───────────────
    @GetMapping("/meetings/batch/{batchId}")
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ZoomMeetingResponse>>> getMeetingsByBatch(
            @PathVariable Long batchId) {

        return ResponseEntity.ok(ApiResponse.success(
                "Batch meetings fetched",
                zoomMeetingService.getMeetingsByBatch(batchId)));
    }

    // ── ADMIN — Get All Meetings by Institute ─────────
    @GetMapping("/meetings/institute/{instituteId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ZoomMeetingResponse>>> getMeetingsByInstitute(
            @PathVariable Long instituteId) {

        return ResponseEntity.ok(ApiResponse.success(
                "Institute meetings fetched",
                zoomMeetingService.getMeetingsByInstitute(instituteId)));
    }

    // ── GET Single Meeting ────────────────────────────
    @GetMapping("/meetings/{meetingId}")
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ZoomMeetingResponse>> getMeeting(
            @PathVariable Long meetingId) {

        return ResponseEntity.ok(ApiResponse.success(
                "Meeting fetched",
                zoomMeetingService.getMeetingById(meetingId)));
    }

    // ── INSTRUCTOR — Delete Meeting ───────────────────
    @DeleteMapping("/meetings/{meetingId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteMeeting(
            @PathVariable Long meetingId,
            HttpServletRequest httpRequest) {

        Long userId = extractUserId(httpRequest);
        zoomMeetingService.deleteMeeting(meetingId, userId);
        return ResponseEntity.ok(ApiResponse.success(
                "Meeting deleted successfully", null));
    }

    // ── ADMIN — Update Meeting Status ─────────────────
    @PatchMapping("/meetings/{meetingId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ZoomMeetingResponse>> updateStatus(
            @PathVariable Long meetingId,
            @RequestParam ZoomMeetingStatus status) {

        return ResponseEntity.ok(ApiResponse.success(
                "Status updated",
                zoomMeetingService.updateMeetingStatus(meetingId, status)));
    }

    // ── HELPER — Extract User ID from JWT ─────────────
    private Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new RuntimeException("Authorization token missing");
    }
}