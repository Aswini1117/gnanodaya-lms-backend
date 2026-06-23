package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.Batch;
import com.gnanodaya.lms.entity.Enrollment;
import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.enums.BatchStatus;
import com.gnanodaya.lms.repository.BatchRepository;
import com.gnanodaya.lms.repository.EnrollmentRepository;
import com.gnanodaya.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final BatchRepository batchRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public Batch createBatch(Batch batch) {
        batch.setStatus(BatchStatus.UPCOMING);
        return batchRepository.save(batch);
    }

    public List<Batch> getBatchesByInstitute(Long instituteId) {
        return batchRepository.findByInstituteId(instituteId);
    }

    public List<Batch> getBatchesByInstructor(Long instructorId) {
        return batchRepository.findByInstructorId(instructorId);
    }

    public Batch getBatchById(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
    }

    public Batch updateBatchStatus(Long id, String status) {
        Batch batch = getBatchById(id);
        batch.setStatus(BatchStatus.valueOf(status.toUpperCase()));
        return batchRepository.save(batch);
    }

    public void deleteBatch(Long id) {
        batchRepository.deleteById(id);
    }

    public Enrollment enrollStudent(Long studentId, Long batchId) {
        if (enrollmentRepository.existsByStudentIdAndBatchId(studentId, batchId)) {
            throw new RuntimeException("Student already enrolled in this batch");
        }
        Batch batch = getBatchById(batchId);
        long currentCount = enrollmentRepository.countByBatchId(batchId);
        if (batch.getMaxStudents() != null && currentCount >= batch.getMaxStudents()) {
            throw new RuntimeException("Batch is full");
        }
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return enrollmentRepository.save(Enrollment.builder()
                .student(student)
                .batch(batch)
                .isActive(true)
                .build());
    }

    public void unenrollStudent(Long studentId, Long batchId) {
        Enrollment enrollment = enrollmentRepository
                .findByStudentIdAndBatchId(studentId, batchId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollmentRepository.delete(enrollment);
    }

    public List<Enrollment> getStudentsInBatch(Long batchId) {
        return enrollmentRepository.findByBatchId(batchId);
    }
}