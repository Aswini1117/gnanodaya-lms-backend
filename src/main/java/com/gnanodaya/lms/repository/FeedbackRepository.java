package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByBatchId(Long batchId);
    List<Feedback> findByInstructorId(Long instructorId);
    List<Feedback> findByCourseId(Long courseId);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.instructor.id = :instructorId")
    Double getAvgRatingByInstructor(Long instructorId);
}