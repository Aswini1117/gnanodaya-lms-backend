package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByBatchId(Long batchId);

    Optional<Enrollment> findByStudentIdAndBatchId(
            Long studentId, Long batchId);

    boolean existsByStudentIdAndBatchId(
            Long studentId, Long batchId);

    long countByBatchId(Long batchId);

    // ← FIXED — removed OrderByCreatedAtDesc
    Optional<Enrollment> findFirstByStudentId(Long studentId);
}