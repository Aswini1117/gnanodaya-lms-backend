package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudentId(Long studentId);
    List<Submission> findByAssignmentId(Long assignmentId);
    boolean existsByStudentIdAndAssignmentId(Long studentId, Long assignmentId);
}