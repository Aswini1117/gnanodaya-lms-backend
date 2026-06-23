package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.CodingSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CodingSubmissionRepository extends JpaRepository<CodingSubmission, Long> {
    List<CodingSubmission> findByStudentIdOrderBySubmittedAtDesc(Long studentId);
    List<CodingSubmission> findByQuestionId(Long questionId);
    List<CodingSubmission> findByStudentIdAndQuestionId(Long studentId, Long questionId);
    List<CodingSubmission> findByStudentIdAndVerdict(Long studentId, String verdict);
    long countByStudentIdAndVerdict(Long studentId, String verdict);
}