package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.Instructor;
import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.repository.BatchRepository;
import com.gnanodaya.lms.repository.EnrollmentRepository;
import com.gnanodaya.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final UserRepository userRepository;
    private final BatchRepository batchRepository;
    private final EnrollmentRepository enrollmentRepository;

    public User getInstructorById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
    }

    public User updateProfile(Long id, User updated) {
        User instructor = getInstructorById(id);
        instructor.setFullName(updated.getFullName());
        instructor.setEmail(updated.getEmail());
        instructor.setProfileImage(updated.getProfileImage());
        return userRepository.save(instructor);
    }

    public Map<String, Object> getDashboardStats(Long instructorId) {
        long totalBatches = batchRepository
                .findByInstructorId(instructorId).size();
        long activeBatches = batchRepository
                .findByInstructorId(instructorId)
                .stream()
                .filter(b -> b.getStatus().name().equals("ONGOING"))
                .count();
        long completedBatches = batchRepository
                .findByInstructorId(instructorId)
                .stream()
                .filter(b -> b.getStatus().name().equals("COMPLETED"))
                .count();
        long totalStudents = batchRepository
                .findByInstructorId(instructorId)
                .stream()
                .mapToLong(b -> enrollmentRepository.countByBatchId(b.getId()))
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBatches", totalBatches);
        stats.put("activeBatches", activeBatches);
        stats.put("completedBatches", completedBatches);
        stats.put("totalStudents", totalStudents);
        return stats;
    }
}