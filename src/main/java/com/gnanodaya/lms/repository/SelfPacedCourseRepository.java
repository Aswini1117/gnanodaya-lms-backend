package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.SelfPacedCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SelfPacedCourseRepository extends JpaRepository<SelfPacedCourse, Long> {
    List<SelfPacedCourse> findByInstituteId(Long instituteId);
    List<SelfPacedCourse> findByInstituteIdAndStatus(Long instituteId, String status);
}