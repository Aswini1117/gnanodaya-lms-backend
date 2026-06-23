package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.BatchAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchAssignmentRepository extends JpaRepository<BatchAssignment, Long> {
    List<BatchAssignment> findByBatchId(Long batchId);
    List<BatchAssignment> findByCreatedById(Long instructorId);
    List<BatchAssignment> findByBatchIdAndStatus(Long batchId, String status);
}