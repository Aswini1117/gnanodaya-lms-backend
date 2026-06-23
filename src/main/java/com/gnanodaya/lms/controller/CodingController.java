package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.CodingQuestion;
import com.gnanodaya.lms.entity.CodingSubmission;
import com.gnanodaya.lms.service.CodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coding")
@RequiredArgsConstructor
public class CodingController {

    private final CodingService codingService;

    // ─── QUESTIONS (Instructor / Admin manages) ───────────────────────

    @PostMapping("/questions")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CodingQuestion>> createQuestion(@RequestBody CodingQuestion question) {
        return ResponseEntity.ok(ApiResponse.success("Question created", codingService.createQuestion(question)));
    }

    @GetMapping("/questions/institute/{instituteId}")
    public ResponseEntity<ApiResponse<List<CodingQuestion>>> getQuestions(@PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Questions", codingService.getQuestionsByInstitute(instituteId)));
    }

    @GetMapping("/questions/institute/{instituteId}/difficulty/{difficulty}")
    public ResponseEntity<ApiResponse<List<CodingQuestion>>> getByDifficulty(
            @PathVariable Long instituteId, @PathVariable String difficulty) {
        return ResponseEntity.ok(ApiResponse.success("Questions",
                codingService.getQuestionsByDifficulty(instituteId, difficulty)));
    }

    @GetMapping("/questions/institute/{instituteId}/topic/{topic}")
    public ResponseEntity<ApiResponse<List<CodingQuestion>>> getByTopic(
            @PathVariable Long instituteId, @PathVariable String topic) {
        return ResponseEntity.ok(ApiResponse.success("Questions",
                codingService.getQuestionsByTopic(instituteId, topic)));
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<ApiResponse<CodingQuestion>> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Question", codingService.getQuestionById(id)));
    }

    @DeleteMapping("/questions/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable Long id) {
        codingService.deleteQuestion(id);
        return ResponseEntity.ok(ApiResponse.success("Question deleted", null));
    }

    // ─── SUBMISSIONS (Student submits, Instructor reviews) ────────────

    @PostMapping("/submissions")
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CodingSubmission>> submitCode(@RequestBody CodingSubmission submission) {
        return ResponseEntity.ok(ApiResponse.success("Code submitted", codingService.submitCode(submission)));
    }

    @GetMapping("/submissions/student/{studentId}")
    public ResponseEntity<ApiResponse<List<CodingSubmission>>> getStudentSubmissions(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("Submissions", codingService.getStudentSubmissions(studentId)));
    }

    @GetMapping("/submissions/question/{questionId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<CodingSubmission>>> getByQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(ApiResponse.success("Submissions", codingService.getSubmissionsByQuestion(questionId)));
    }

    @GetMapping("/submissions/student/{studentId}/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("Coding stats", codingService.getStudentCodingStats(studentId)));
    }

    @PutMapping("/submissions/{submissionId}/verdict")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CodingSubmission>> updateVerdict(
            @PathVariable Long submissionId, @RequestParam String verdict) {
        return ResponseEntity.ok(ApiResponse.success("Verdict updated",
                codingService.updateVerdict(submissionId, verdict)));
    }
}