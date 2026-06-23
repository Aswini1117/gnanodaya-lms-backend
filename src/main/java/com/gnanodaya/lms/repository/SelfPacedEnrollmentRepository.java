package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.SelfPacedEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SelfPacedEnrollmentRepository extends JpaRepository<SelfPacedEnrollment, Long> {
    List<SelfPacedEnrollment> findByStudentId(Long studentId);
    List<SelfPacedEnrollment> findBySelfPacedCourseId(Long courseId);
    Optional<SelfPacedEnrollment> findByStudentIdAndSelfPacedCourseId(Long studentId, Long courseId);
    boolean existsByStudentIdAndSelfPacedCourseId(Long studentId, Long courseId);
}