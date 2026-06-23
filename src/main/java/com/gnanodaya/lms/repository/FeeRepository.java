package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.Fee;
import com.gnanodaya.lms.enums.FeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {
    List<Fee> findByStudentId(Long studentId);
    List<Fee> findByBatchId(Long batchId);
    List<Fee> findByStatus(FeeStatus status);

    @Query("SELECT SUM(f.paidAmount) FROM Fee f WHERE f.batch.institute.id = :instituteId")
    BigDecimal getTotalCollectedByInstitute(Long instituteId);

    @Query("SELECT SUM(f.pendingAmount) FROM Fee f WHERE f.batch.institute.id = :instituteId")
    BigDecimal getTotalPendingByInstitute(Long instituteId);
}