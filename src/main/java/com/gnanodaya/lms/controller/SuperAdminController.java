package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.request.CreateUserRequest;
import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.Institute;
import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.repository.InstituteRepository;
import com.gnanodaya.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    private final InstituteRepository instituteRepository;
    private final UserService userService;

    @GetMapping("/institutes")
    public ResponseEntity<ApiResponse<List<Institute>>> getAllInstitutes() {
        return ResponseEntity.ok(ApiResponse.success("Institutes", instituteRepository.findAll()));
    }

    @PostMapping("/institutes")
    public ResponseEntity<ApiResponse<Institute>> createInstitute(@RequestBody Institute institute) {
        institute.setIsActive(true);
        return ResponseEntity.ok(ApiResponse.success("Institute created", instituteRepository.save(institute)));
    }

    @PutMapping("/institutes/{id}/toggle")
    public ResponseEntity<ApiResponse<Institute>> toggleInstitute(@PathVariable Long id) {
        Institute institute = instituteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institute not found"));
        institute.setIsActive(!institute.getIsActive());
        return ResponseEntity.ok(ApiResponse.success("Status updated", instituteRepository.save(institute)));
    }

    @PostMapping("/admins")
    public ResponseEntity<ApiResponse<User>> createAdmin(@RequestBody CreateUserRequest request) {
        request.setRole("ADMIN");
        return ResponseEntity.ok(ApiResponse.success("Admin created", userService.createUser(request)));
    }

    // ── GET ADMINS BY INSTITUTE ────────────────────────────
    @GetMapping("/admins/{instituteId}")
    public ResponseEntity<ApiResponse<List<User>>> getAdminsByInstitute(
            @PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Admins",
                userService.getUsersByInstituteAndRole(instituteId, "ADMIN")));
    }
}