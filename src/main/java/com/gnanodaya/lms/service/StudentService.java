package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.repository.AttendanceRepository;
import com.gnanodaya.lms.repository.EnrollmentRepository;
import com.gnanodaya.lms.repository.FeeRepository;
import com.gnanodaya.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final FeeRepository feeRepository;

    public User getStudentById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public User updateProfile(Long id, User updated) {
        User student = getStudentById(id);
        student.setFullName(updated.getFullName());
        student.setEmail(updated.getEmail());
        student.setProfileImage(updated.getProfileImage());
        return userRepository.save(student);
    }

    public Map<String, Object> getDashboardStats(Long studentId) {
        long totalEnrollments = enrollmentRepository
                .findByStudentId(studentId).size();
        long totalFees = feeRepository
                .findByStudentId(studentId).size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEnrollments", totalEnrollments);
        stats.put("totalFees", totalFees);
        return stats;
    }
}