package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.LearningProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LearningProgressRepository extends JpaRepository<LearningProgress, Long> {
    List<LearningProgress> findByStudentId(Long studentId);
    List<LearningProgress> findByStudentIdAndBatchId(Long studentId, Long batchId);
    Optional<LearningProgress> findByStudentIdAndContentId(Long studentId, Long contentId);

    @Query("SELECT COUNT(lp) FROM LearningProgress lp WHERE lp.student.id = :studentId AND lp.batch.id = :batchId AND lp.isCompleted = true")
    long countCompletedByStudentAndBatch(Long studentId, Long batchId);
}