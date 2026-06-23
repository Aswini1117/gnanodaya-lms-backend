package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.Assignment;
import com.gnanodaya.lms.entity.Submission;
import com.gnanodaya.lms.repository.AssignmentRepository;
import com.gnanodaya.lms.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> getAssignmentsByBatch(Long batchId) {
        return assignmentRepository.findByBatchId(batchId);
    }

    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    public Submission submitAssignment(Submission submission) {
        submission.setIsGraded(false);
        return submissionRepository.save(submission);
    }

    public List<Submission> getSubmissionsByAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    public List<Submission> getStudentSubmissions(Long studentId) {
        return submissionRepository.findByStudentId(studentId);
    }

    public Submission gradeSubmission(Long submissionId,
                                      Integer marks, String feedback) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        submission.setMarksObtained(marks);
        submission.setFeedback(feedback);
        submission.setIsGraded(true);
        return submissionRepository.save(submission);
    }
}