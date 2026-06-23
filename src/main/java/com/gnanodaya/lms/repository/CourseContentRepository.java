package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {
    List<CourseContent> findByCourseIdOrderByOrderIndex(Long courseId);
    List<CourseContent> findByBatchIdOrderByOrderIndex(Long batchId);
}