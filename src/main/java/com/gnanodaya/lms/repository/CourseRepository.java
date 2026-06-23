package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.Course;
import com.gnanodaya.lms.enums.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstituteId(Long instituteId);
    List<Course> findByInstituteIdAndStatus(Long instituteId, CourseStatus status);
}