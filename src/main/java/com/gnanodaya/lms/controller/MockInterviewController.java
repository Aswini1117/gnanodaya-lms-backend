package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.MockInterview;
import com.gnanodaya.lms.repository.MockInterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mock-interviews")
@RequiredArgsConstructor
public class MockInterviewController {

    private final MockInterviewRepository mockInterviewRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<MockInterview>> startInterview(
            @RequestBody MockInterview interview) {
        interview.setStatus("IN_PROGRESS");
        return ResponseEntity.ok(ApiResponse.success("Interview started",
                mockInterviewRepository.save(interview)));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<MockInterview>> completeInterview(
            @PathVariable Long id, @RequestBody MockInterview result) {
        MockInterview interview = mockInterviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        interview.setScore(result.getScore());
        interview.setFeedback(result.getFeedback());
        interview.setDurationMinutes(result.getDurationMinutes());
        interview.setStatus("COMPLETED");
        return ResponseEntity.ok(ApiResponse.success("Interview completed",
                mockInterviewRepository.save(interview)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<MockInterview>>> getStudentInterviews(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("Interview history",
                mockInterviewRepository
                        .findByStudentIdOrderByAttemptedAtDesc(studentId)));
    }
}