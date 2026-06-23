package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.MockInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MockInterviewRepository extends JpaRepository<MockInterview, Long> {
    List<MockInterview> findByStudentIdOrderByAttemptedAtDesc(Long studentId);
    List<MockInterview> findByStudentIdAndStatus(Long studentId, String status);
}