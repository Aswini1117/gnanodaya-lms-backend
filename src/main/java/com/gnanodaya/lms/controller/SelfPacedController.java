package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.SelfPacedCourse;
import com.gnanodaya.lms.entity.SelfPacedEnrollment;
import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.repository.SelfPacedCourseRepository;
import com.gnanodaya.lms.repository.SelfPacedEnrollmentRepository;
import com.gnanodaya.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/self-paced")
@RequiredArgsConstructor
public class SelfPacedController {

    private final SelfPacedCourseRepository courseRepository;
    private final SelfPacedEnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<SelfPacedCourse>> createCourse(
            @RequestBody SelfPacedCourse course) {
        course.setStatus("ACTIVE");
        return ResponseEntity.ok(ApiResponse.success("Self-paced course created",
                courseRepository.save(course)));
    }

    @GetMapping("/institute/{instituteId}")
    public ResponseEntity<ApiResponse<List<SelfPacedCourse>>> getCourses(
            @PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Courses",
                courseRepository.findByInstituteIdAndStatus(instituteId, "ACTIVE")));
    }

    @PostMapping("/enroll")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<SelfPacedEnrollment>> enroll(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        if (enrollmentRepository.existsByStudentIdAndSelfPacedCourseId(studentId, courseId)) {
            throw new RuntimeException("Already enrolled in this course");
        }
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        SelfPacedCourse course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        SelfPacedEnrollment enrollment = SelfPacedEnrollment.builder()
                .student(student)
                .selfPacedCourse(course)
                .progressPercent(0)
                .status("ACTIVE")
                .build();
        return ResponseEntity.ok(ApiResponse.success("Enrolled",
                enrollmentRepository.save(enrollment)));
    }

    @GetMapping("/my-enrollments/{studentId}")
    public ResponseEntity<ApiResponse<List<SelfPacedEnrollment>>> getMyEnrollments(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("My enrollments",
                enrollmentRepository.findByStudentId(studentId)));
    }
}