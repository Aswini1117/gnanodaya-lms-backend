package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.BatchAssignment;
import com.gnanodaya.lms.repository.BatchAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/batch-assignments")
@RequiredArgsConstructor
public class BatchAssignmentController {

    private final BatchAssignmentRepository batchAssignmentRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<BatchAssignment>> create(
            @RequestBody BatchAssignment assignment) {
        assignment.setStatus("ACTIVE");
        return ResponseEntity.ok(ApiResponse.success("Batch assignment created",
                batchAssignmentRepository.save(assignment)));
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<ApiResponse<List<BatchAssignment>>> getByBatch(
            @PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Batch assignments",
                batchAssignmentRepository.findByBatchId(batchId)));
    }

    @GetMapping("/instructor/{instructorId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<BatchAssignment>>> getByInstructor(
            @PathVariable Long instructorId) {
        return ResponseEntity.ok(ApiResponse.success("My assignments",
                batchAssignmentRepository.findByCreatedById(instructorId)));
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<BatchAssignment>> closeAssignment(
            @PathVariable Long id) {
        BatchAssignment assignment = batchAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setStatus("CLOSED");
        return ResponseEntity.ok(ApiResponse.success("Assignment closed",
                batchAssignmentRepository.save(assignment)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        batchAssignmentRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted", null));
    }
}