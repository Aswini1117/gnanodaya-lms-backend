package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.*;
import com.gnanodaya.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/instructor")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
public class InstructorController {

    private final BatchRepository batchRepository;
    private final AttendanceRepository attendanceRepository;
    private final AssignmentRepository assignmentRepository;
    private final EnrollmentRepository enrollmentRepository;

    // ── My Batches ────────────────────────────────────
    @GetMapping("/my-batches/{instructorId}")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMyBatches(
            @PathVariable Long instructorId) {

        List<Batch> batches =
                batchRepository.findByInstructorId(instructorId);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Batch b : batches) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", b.getId());
            map.put("batchName", b.getBatchName());
            map.put("status", b.getStatus() != null
                    ? b.getStatus().name() : "ACTIVE");
            map.put("courseName", b.getCourse() != null
                    ? b.getCourse().getTitle() : "");
            result.add(map);
        }

        return ResponseEntity.ok(
                ApiResponse.success("My batches", result));
    }

    // ── Students in a batch ───────────────────────────
    @GetMapping("/my-batches/{batchId}/students")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getStudentsInBatch(
            @PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Students",
                enrollmentRepository.findByBatchId(batchId)));
    }

    // ── Mark attendance ───────────────────────────────
    @PostMapping("/attendance")
    public ResponseEntity<ApiResponse<Attendance>> markAttendance(
            @RequestBody Attendance attendance) {
        attendance.setAttendanceDate(LocalDate.now());
        return ResponseEntity.ok(ApiResponse.success(
                "Attendance marked",
                attendanceRepository.save(attendance)));
    }

    // ── Get attendance for a batch on a date ──────────
    @GetMapping("/attendance/{batchId}")
    public ResponseEntity<ApiResponse<List<Attendance>>> getAttendance(
            @PathVariable Long batchId,
            @RequestParam String date) {
        return ResponseEntity.ok(ApiResponse.success(
                "Attendance",
                attendanceRepository.findByBatchIdAndAttendanceDate(
                        batchId, LocalDate.parse(date))));
    }

    // ── Create assignment ─────────────────────────────
    @PostMapping("/assignments")
    public ResponseEntity<ApiResponse<Assignment>> createAssignment(
            @RequestBody Assignment assignment) {
        return ResponseEntity.ok(ApiResponse.success(
                "Assignment created",
                assignmentRepository.save(assignment)));
    }

    // ── Get assignments for a batch ───────────────────
    @GetMapping("/assignments/{batchId}")
    public ResponseEntity<ApiResponse<List<Assignment>>> getAssignments(
            @PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Assignments",
                assignmentRepository.findByBatchId(batchId)));
    }
}