package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.Batch;
import com.gnanodaya.lms.entity.Enrollment;
import com.gnanodaya.lms.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Batch>> createBatch(@RequestBody Batch batch) {
        return ResponseEntity.ok(ApiResponse.success("Batch created",
                batchService.createBatch(batch)));
    }

    @GetMapping("/institute/{instituteId}")
    public ResponseEntity<ApiResponse<List<Batch>>> getBatchesByInstitute(
            @PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Batches",
                batchService.getBatchesByInstitute(instituteId)));
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<ApiResponse<List<Batch>>> getBatchesByInstructor(
            @PathVariable Long instructorId) {
        return ResponseEntity.ok(ApiResponse.success("My batches",
                batchService.getBatchesByInstructor(instructorId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Batch>> getBatch(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Batch",
                batchService.getBatchById(id)));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Batch>> updateStatus(
            @PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("Status updated",
                batchService.updateBatchStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.ok(ApiResponse.success("Batch deleted", null));
    }

    @PostMapping("/{batchId}/enroll/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Enrollment>> enrollStudent(
            @PathVariable Long batchId, @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("Student enrolled",
                batchService.enrollStudent(studentId, batchId)));
    }

    @DeleteMapping("/{batchId}/unenroll/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> unenrollStudent(
            @PathVariable Long batchId, @PathVariable Long studentId) {
        batchService.unenrollStudent(studentId, batchId);
        return ResponseEntity.ok(ApiResponse.success("Student removed", null));
    }

    @GetMapping("/{batchId}/students")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getStudents(
            @PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Students in batch",
                batchService.getStudentsInBatch(batchId)));
    }
}