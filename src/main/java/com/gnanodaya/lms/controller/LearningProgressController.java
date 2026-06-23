package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.LearningProgress;
import com.gnanodaya.lms.service.LearningProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/learning-progress")
@RequiredArgsConstructor
public class LearningProgressController {

    private final LearningProgressService progressService;

    // Student updates watch progress on a video
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<LearningProgress>> updateProgress(
            @RequestParam Long studentId,
            @RequestParam Long contentId,
            @RequestParam Integer watchedDuration) {
        return ResponseEntity.ok(ApiResponse.success("Progress updated",
                progressService.updateProgress(studentId, contentId, watchedDuration)));
    }

    // Get student's progress in a batch
    @GetMapping("/student/{studentId}/batch/{batchId}")
    public ResponseEntity<ApiResponse<List<LearningProgress>>> getProgress(
            @PathVariable Long studentId, @PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Learning progress",
                progressService.getStudentProgress(studentId, batchId)));
    }

    // Get summary — how much % done in a batch
    @GetMapping("/summary/student/{studentId}/batch/{batchId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSummary(
            @PathVariable Long studentId, @PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Progress summary",
                progressService.getBatchProgressSummary(studentId, batchId)));
    }
}