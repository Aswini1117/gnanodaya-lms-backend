package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByBatchId(Long batchId);
    List<Assignment> findByCreatedById(Long createdById);
}