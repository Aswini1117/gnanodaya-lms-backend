package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.Attendance;
import com.gnanodaya.lms.enums.AttendanceStatus;
import com.gnanodaya.lms.repository.AttendanceRepository;
import com.gnanodaya.lms.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EnrollmentRepository enrollmentRepository;

    public Attendance markAttendance(Attendance attendance) {
        attendance.setAttendanceDate(LocalDate.now());
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByBatchAndDate(Long batchId, LocalDate date) {
        return attendanceRepository.findByBatchIdAndAttendanceDate(batchId, date);
    }

    public List<Attendance> getStudentAttendance(Long studentId, Long batchId) {
        return attendanceRepository.findByStudentIdAndBatchId(studentId, batchId);
    }

    // Calculate attendance percentage for a student in a batch
    public Map<String, Object> getAttendanceStats(Long studentId, Long batchId) {
        long totalClasses = attendanceRepository.countByStudentIdAndBatchId(studentId, batchId);
        long presentCount = attendanceRepository.findByStudentIdAndBatchId(studentId, batchId)
                .stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                .count();

        double percentage = totalClasses > 0 ? (presentCount * 100.0 / totalClasses) : 0;

        return Map.of(
                "totalClasses", totalClasses,
                "present", presentCount,
                "absent", totalClasses - presentCount,
                "percentage", Math.round(percentage * 10.0) / 10.0
        );
    }
}