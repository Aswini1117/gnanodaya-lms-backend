package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.Batch;
import com.gnanodaya.lms.enums.BatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByInstituteId(Long instituteId);
    List<Batch> findByInstructorId(Long instructorId);
    List<Batch> findByCourseId(Long courseId);
    List<Batch> findByInstituteIdAndStatus(Long instituteId, BatchStatus status);
}