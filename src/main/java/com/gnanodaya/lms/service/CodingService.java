package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.CodingQuestion;
import com.gnanodaya.lms.entity.CodingSubmission;
import com.gnanodaya.lms.repository.CodingQuestionRepository;
import com.gnanodaya.lms.repository.CodingSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CodingService {

    private final CodingQuestionRepository questionRepository;
    private final CodingSubmissionRepository submissionRepository;

    public CodingQuestion createQuestion(CodingQuestion question) {
        question.setStatus("ACTIVE");
        return questionRepository.save(question);
    }

    public List<CodingQuestion> getQuestionsByInstitute(Long instituteId) {
        return questionRepository.findByInstituteIdAndStatus(instituteId, "ACTIVE");
    }

    public List<CodingQuestion> getQuestionsByDifficulty(Long instituteId, String difficulty) {
        return questionRepository.findByInstituteIdAndDifficulty(instituteId, difficulty);
    }

    public List<CodingQuestion> getQuestionsByTopic(Long instituteId, String topic) {
        return questionRepository.findByInstituteIdAndTopic(instituteId, topic);
    }

    public CodingQuestion getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public void deleteQuestion(Long id) {
        CodingQuestion question = getQuestionById(id);
        question.setStatus("INACTIVE");
        questionRepository.save(question);
    }

    public CodingSubmission submitCode(CodingSubmission submission) {
        submission.setVerdict("PENDING");
        return submissionRepository.save(submission);
    }

    public List<CodingSubmission> getStudentSubmissions(Long studentId) {
        return submissionRepository.findByStudentIdOrderBySubmittedAtDesc(studentId);
    }

    public List<CodingSubmission> getSubmissionsByQuestion(Long questionId) {
        return submissionRepository.findByQuestionId(questionId);
    }

    public Map<String, Object> getStudentCodingStats(Long studentId) {
        long total = submissionRepository.findByStudentIdOrderBySubmittedAtDesc(studentId).size();
        long accepted = submissionRepository.countByStudentIdAndVerdict(studentId, "ACCEPTED");
        long wrong = submissionRepository.countByStudentIdAndVerdict(studentId, "WRONG_ANSWER");
        return Map.of(
                "totalSubmissions", total,
                "accepted", accepted,
                "wrongAnswer", wrong,
                "successRate", total > 0 ? Math.round((accepted * 100.0 / total) * 10.0) / 10.0 : 0
        );
    }

    public CodingSubmission updateVerdict(Long submissionId, String verdict) {
        CodingSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        submission.setVerdict(verdict);
        return submissionRepository.save(submission);
    }
}