package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.announcement.AnnouncementRequest;
import com.gnanodaya.lms.dto.announcement.AnnouncementResponse;
import com.gnanodaya.lms.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@Tag(name = "Announcements", description = "Create and view announcements for batch/institute")
public class AnnouncementController {

    private final com.gnanodaya.lms.service.AnnouncementService announcementService;

    // ── CREATE ANNOUNCEMENT ────────────────────────────────
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','INSTRUCTOR')")
    @Operation(
            summary = "Create Announcement",
            description = "Admin/Instructor creates an announcement for a batch or whole institute"
    )
    public ResponseEntity<ApiResponse<AnnouncementResponse>> createAnnouncement(
            @RequestBody AnnouncementRequest request) {
        AnnouncementResponse saved = announcementService.createAnnouncement(request);
        return ResponseEntity.ok(
                ApiResponse.success("Announcement created successfully", saved));
    }

    // ── GET BY INSTITUTE ────────────────────────────────────
    @GetMapping("/institute/{instituteId}")
    @Operation(summary = "Get All Institute Announcements")
    public ResponseEntity<ApiResponse<List<AnnouncementResponse>>> getByInstitute(
            @PathVariable Long instituteId) {
        return ResponseEntity.ok(
                ApiResponse.success("Announcements",
                        announcementService.getByInstitute(instituteId)));
    }

    // ── GET BY BATCH ────────────────────────────────────────
    @GetMapping("/batch/{batchId}")
    @Operation(summary = "Get Batch Announcements")
    public ResponseEntity<ApiResponse<List<AnnouncementResponse>>> getByBatch(
            @PathVariable Long batchId) {
        return ResponseEntity.ok(
                ApiResponse.success("Batch announcements",
                        announcementService.getByBatch(batchId)));
    }

    // ── GET BY CREATED USER ─────────────────────────────────
    @GetMapping("/created-by/{userId}")
    @Operation(summary = "Get Announcements Created by a User")
    public ResponseEntity<ApiResponse<List<AnnouncementResponse>>> getByCreatedBy(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                ApiResponse.success("My announcements",
                        announcementService.getByCreatedBy(userId)));
    }

    // ── DELETE ──────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','INSTRUCTOR')")
    @Operation(summary = "Delete Announcement")
    public ResponseEntity<ApiResponse<Void>> deleteAnnouncement(
            @PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok(ApiResponse.success("Announcement deleted", null));
    }
}