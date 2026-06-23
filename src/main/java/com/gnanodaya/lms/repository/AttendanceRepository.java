package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByBatchIdAndAttendanceDate(Long batchId, LocalDate date);
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByStudentIdAndBatchId(Long studentId, Long batchId);
    long countByStudentIdAndBatchId(Long studentId, Long batchId);
}