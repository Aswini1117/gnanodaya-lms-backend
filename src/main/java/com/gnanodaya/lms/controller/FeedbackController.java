package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.Feedback;
import com.gnanodaya.lms.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Feedback>> submitFeedback(
            @RequestBody Feedback feedback) {
        return ResponseEntity.ok(ApiResponse.success("Feedback submitted",
                feedbackRepository.save(feedback)));
    }

    @GetMapping("/batch/{batchId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<Feedback>>> getBatchFeedback(
            @PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Batch feedback",
                feedbackRepository.findByBatchId(batchId)));
    }

    @GetMapping("/instructor/{instructorId}/rating")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInstructorRating(
            @PathVariable Long instructorId) {
        Double avg = feedbackRepository.getAvgRatingByInstructor(instructorId);
        return ResponseEntity.ok(ApiResponse.success("Rating",
                Map.of("averageRating", avg != null ? avg : 0.0)));
    }
}
