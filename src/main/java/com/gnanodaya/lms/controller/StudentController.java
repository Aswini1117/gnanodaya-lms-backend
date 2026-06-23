package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.*;
import com.gnanodaya.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN','SUPER_ADMIN')")
public class StudentController {

    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final FeeRepository feeRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;

    // My enrolled batches
    @GetMapping("/enrollments/{studentId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getMyBatches(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("My batches", enrollmentRepository.findByStudentId(studentId)));
    }

    // My attendance
    @GetMapping("/attendance/{studentId}/{batchId}")
    public ResponseEntity<ApiResponse<List<Attendance>>> getMyAttendance(
            @PathVariable Long studentId, @PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("My attendance",
                attendanceRepository.findByStudentIdAndBatchId(studentId, batchId)));
    }

    // My fees
    @GetMapping("/fees/{studentId}")
    public ResponseEntity<ApiResponse<List<Fee>>> getMyFees(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("My fees", feeRepository.findByStudentId(studentId)));
    }

    // My assignments
    @GetMapping("/assignments/{batchId}")
    public ResponseEntity<ApiResponse<List<Assignment>>> getAssignments(@PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Assignments", assignmentRepository.findByBatchId(batchId)));
    }

    // Submit assignment
    @PostMapping("/assignments/submit")
    public ResponseEntity<ApiResponse<Submission>> submitAssignment(@RequestBody Submission submission) {
        submission.setIsGraded(false);
        return ResponseEntity.ok(ApiResponse.success("Assignment submitted", submissionRepository.save(submission)));
    }

    // My submissions
    @GetMapping("/submissions/{studentId}")
    public ResponseEntity<ApiResponse<List<Submission>>> getMySubmissions(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("My submissions", submissionRepository.findByStudentId(studentId)));
    }
}