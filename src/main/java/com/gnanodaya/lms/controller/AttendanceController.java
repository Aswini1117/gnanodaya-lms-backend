package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.Attendance;
import com.gnanodaya.lms.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Attendance>> markAttendance(
            @RequestBody Attendance attendance) {
        return ResponseEntity.ok(ApiResponse.success("Attendance marked",
                attendanceService.markAttendance(attendance)));
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<ApiResponse<List<Attendance>>> getByBatchAndDate(
            @PathVariable Long batchId,
            @RequestParam(required = false) String date) {
        LocalDate localDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        return ResponseEntity.ok(ApiResponse.success("Attendance",
                attendanceService.getAttendanceByBatchAndDate(batchId, localDate)));
    }

    @GetMapping("/student/{studentId}/batch/{batchId}")
    public ResponseEntity<ApiResponse<List<Attendance>>> getStudentAttendance(
            @PathVariable Long studentId,
            @PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Student attendance",
                attendanceService.getStudentAttendance(studentId, batchId)));
    }

    @GetMapping("/stats/{studentId}/batch/{batchId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats(
            @PathVariable Long studentId,
            @PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Attendance stats",
                attendanceService.getAttendanceStats(studentId, batchId)));
    }
}