package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.request.CreateUserRequest;
import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.*;
import com.gnanodaya.lms.enums.UserStatus;
import com.gnanodaya.lms.repository.*;
import com.gnanodaya.lms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
@Tag(name = "Admin", description = "Admin APIs — Manage students, instructors, courses, batches")
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final InstituteRepository instituteRepository;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final FeeRepository feeRepository;

    // ── PENDING STUDENTS ───────────────────────────────────
    @GetMapping("/students/pending/{instituteId}")
    @Operation(
            summary = "Get Pending Students",
            description = "Returns all students waiting for Admin approval (status = PENDING)"
    )
    public ResponseEntity<ApiResponse<List<User>>> getPendingStudents(
            @PathVariable Long instituteId) {
        List<User> pendingStudents = userRepository
                .findByStatusAndRole(UserStatus.PENDING,
                        com.gnanodaya.lms.enums.Role.STUDENT);
        return ResponseEntity.ok(
                ApiResponse.success("Pending students", pendingStudents));
    }

    // ── GIVE ACCESS TO STUDENT ─────────────────────────────
    @PutMapping("/students/{studentId}/give-access")
    @Operation(
            summary = "Give Access to Student",
            description = """
            Approves a pending student registration.
            Changes student status from PENDING → ACTIVE.
            After this the student can login.
            Also assigns student to the institute.
            """
    )
    public ResponseEntity<ApiResponse<User>> giveAccess(
            @PathVariable Long studentId,
            @RequestParam Long instituteId) {
        try {
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            if (student.getStatus() == UserStatus.ACTIVE) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Student already has access"));
            }

            // Set status to ACTIVE
            student.setStatus(UserStatus.ACTIVE);

            // Assign to institute
            Institute institute = instituteRepository.findById(instituteId)
                    .orElseThrow(() -> new RuntimeException("Institute not found"));
            student.setInstitute(institute);

            User updatedStudent = userRepository.save(student);

            return ResponseEntity.ok(ApiResponse.success(
                    "Access granted! Student can now login.", updatedStudent));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // ── REVOKE ACCESS ──────────────────────────────────────
    @PutMapping("/students/{studentId}/revoke-access")
    @Operation(
            summary = "Revoke Student Access",
            description = "Deactivates a student account. Student cannot login after this."
    )
    public ResponseEntity<ApiResponse<User>> revokeAccess(
            @PathVariable Long studentId) {
        try {
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            student.setStatus(UserStatus.INACTIVE);
            User updatedStudent = userRepository.save(student);
            return ResponseEntity.ok(ApiResponse.success(
                    "Access revoked.", updatedStudent));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // ── INSTRUCTORS ────────────────────────────────────────
    @PostMapping("/instructors")
    @Operation(
            summary = "Create Instructor",
            description = "Admin creates instructor account. Instructor cannot self-register."
    )
    public ResponseEntity<ApiResponse<User>> createInstructor(
            @RequestBody CreateUserRequest request) {
        try {
            request.setRole("INSTRUCTOR");
            User instructor = userService.createUser(request);
            return ResponseEntity.ok(
                    ApiResponse.success("Instructor created successfully", instructor));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/instructors/{instituteId}")
    @Operation(summary = "Get All Instructors")
    public ResponseEntity<ApiResponse<List<User>>> getInstructors(
            @PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Instructors",
                userService.getUsersByInstituteAndRole(instituteId, "INSTRUCTOR")));
    }

    // ── STUDENTS ───────────────────────────────────────────
    @PostMapping("/students")
    @Operation(
            summary = "Create Student (Admin)",
            description = "Admin directly creates a student with ACTIVE status."
    )
    public ResponseEntity<ApiResponse<User>> createStudent(
            @RequestBody CreateUserRequest request) {
        try {
            request.setRole("STUDENT");
            User student = userService.createUser(request);
            return ResponseEntity.ok(
                    ApiResponse.success("Student created successfully", student));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/students/{instituteId}")
    @Operation(summary = "Get All Active Students")
    public ResponseEntity<ApiResponse<List<User>>> getStudents(
            @PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Students",
                userService.getUsersByInstituteAndRole(instituteId, "STUDENT")));
    }

    // ── USER STATUS ────────────────────────────────────────
    @PutMapping("/users/{id}/status")
    @Operation(summary = "Update User Status")
    public ResponseEntity<ApiResponse<User>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("Status updated",
                userService.updateUserStatus(id, status)));
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete User")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted", null));
    }

    // ── COURSES ────────────────────────────────────────────
    @GetMapping("/courses/{instituteId}")
    @Operation(summary = "Get All Courses")
    public ResponseEntity<ApiResponse<List<Course>>> getCourses(
            @PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Courses",
                courseRepository.findByInstituteId(instituteId)));
    }

    @PostMapping("/courses")
    @Operation(summary = "Create Course")
    public ResponseEntity<ApiResponse<Course>> createCourse(
            @RequestBody Course course) {
        return ResponseEntity.ok(ApiResponse.success("Course created",
                courseRepository.save(course)));
    }

    // ── BATCHES ────────────────────────────────────────────
    @GetMapping("/batches/{instituteId}")
    @Operation(summary = "Get All Batches")
    public ResponseEntity<ApiResponse<List<Batch>>> getBatches(
            @PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Batches",
                batchRepository.findByInstituteId(instituteId)));
    }

    @PostMapping("/batches")
    @Operation(summary = "Create Batch")
    public ResponseEntity<ApiResponse<Batch>> createBatch(
            @RequestBody Batch batch) {
        return ResponseEntity.ok(ApiResponse.success("Batch created",
                batchRepository.save(batch)));
    }

    // ── DASHBOARD ──────────────────────────────────────────
    @GetMapping("/dashboard/{instituteId}")
    @Operation(
            summary = "Get Admin Dashboard Stats",
            description = "Returns total students, instructors, courses, batches, fees collected and pending"
    )
    public ResponseEntity<ApiResponse<?>> getDashboard(
            @PathVariable Long instituteId) {

        long students    = userService.getUsersByInstituteAndRole(instituteId, "STUDENT").size();
        long instructors = userService.getUsersByInstituteAndRole(instituteId, "INSTRUCTOR").size();
        long courses     = courseRepository.findByInstituteId(instituteId).size();
        long batches     = batchRepository.findByInstituteId(instituteId).size();
        long pending     = userRepository.findByStatusAndRole(
                UserStatus.PENDING,
                com.gnanodaya.lms.enums.Role.STUDENT).size();

        var stats = new java.util.HashMap<String, Object>();
        stats.put("totalStudents",    students);
        stats.put("totalInstructors", instructors);
        stats.put("totalCourses",     courses);
        stats.put("totalBatches",     batches);
        stats.put("pendingStudents",  pending);
        stats.put("totalCollected",   feeRepository.getTotalCollectedByInstitute(instituteId));
        stats.put("totalPending",     feeRepository.getTotalPendingByInstitute(instituteId));

        return ResponseEntity.ok(ApiResponse.success("Dashboard stats", stats));
    }
}