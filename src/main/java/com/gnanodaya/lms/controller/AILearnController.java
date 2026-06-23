package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.AILearnSession;
import com.gnanodaya.lms.repository.AILearnSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-learn")
@RequiredArgsConstructor
public class AILearnController {

    private final AILearnSessionRepository aiLearnSessionRepository;

    // Start a new AI session (LEARN or MOCK_INTERVIEW)
    @PostMapping("/session")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AILearnSession>> startSession(@RequestBody AILearnSession session) {
        session.setStatus("IN_PROGRESS");
        return ResponseEntity.ok(ApiResponse.success("Session started",
                aiLearnSessionRepository.save(session)));
    }

    // Complete the session with score and feedback
    @PutMapping("/session/{id}/complete")
    public ResponseEntity<ApiResponse<AILearnSession>> completeSession(
            @PathVariable Long id, @RequestBody AILearnSession result) {
        AILearnSession session = aiLearnSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setStatus("COMPLETED");
        session.setScore(result.getScore());
        session.setFeedback(result.getFeedback());
        session.setDurationMinutes(result.getDurationMinutes());
        session.setAiResponse(result.getAiResponse());
        return ResponseEntity.ok(ApiResponse.success("Session completed",
                aiLearnSessionRepository.save(session)));
    }

    // AI History — all past sessions
    @GetMapping("/history/{studentId}")
    public ResponseEntity<ApiResponse<List<AILearnSession>>> getHistory(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("AI history",
                aiLearnSessionRepository.findByStudentIdOrderByCreatedAtDesc(studentId)));
    }

    // Filter by type: LEARN or MOCK_INTERVIEW
    @GetMapping("/history/{studentId}/type/{type}")
    public ResponseEntity<ApiResponse<List<AILearnSession>>> getByType(
            @PathVariable Long studentId, @PathVariable String type) {
        return ResponseEntity.ok(ApiResponse.success("Sessions",
                aiLearnSessionRepository.findByStudentIdAndSessionType(studentId, type)));
    }
}
