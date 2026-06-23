package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.AILearnSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AILearnSessionRepository extends JpaRepository<AILearnSession, Long> {
    List<AILearnSession> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<AILearnSession> findByStudentIdAndSessionType(Long studentId, String sessionType);
    List<AILearnSession> findByStudentIdAndStatus(Long studentId, String status);
}