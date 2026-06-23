package com.gnanodaya.lms.service;

import com.gnanodaya.lms.dto.request.LoginRequest;
import com.gnanodaya.lms.dto.response.AuthResponse;
import com.gnanodaya.lms.entity.Batch;
import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.enums.Role;
import com.gnanodaya.lms.enums.UserStatus;
import com.gnanodaya.lms.repository.EnrollmentRepository;
import com.gnanodaya.lms.repository.UserRepository;
import com.gnanodaya.lms.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnrollmentRepository enrollmentRepository;

    public AuthResponse login(LoginRequest request) {

        // Step 1 — Find user by phone
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new RuntimeException(
                        "No account found with this phone number"));

        // Step 2 — Check role matches
        Role requestedRole = Role.valueOf(
                request.getRole().toUpperCase());
        if (!user.getRole().equals(requestedRole)) {
            throw new RuntimeException(
                    "You are not registered as " + request.getRole() +
                            ". Your role is " + user.getRole().name());
        }

        // Step 3 — Check account status
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new RuntimeException(
                    "Your account is inactive. Please contact admin.");
        }
        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new RuntimeException(
                    "Your account is suspended. Please contact admin.");
        }
        if (user.getStatus() == UserStatus.PENDING) {
            throw new RuntimeException(
                    "Your account is pending approval. " +
                            "Please wait for admin to activate your account.");
        }

        // Step 4 — Verify password
        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        // Step 5 — Generate JWT
        String token = jwtUtil.generateToken(
                user.getId(),
                user.getPhone(),
                user.getRole().name());

        // Step 6 — Get batchId for student
        Long batchId = null;
        if (user.getRole() == Role.STUDENT) {
            batchId = enrollmentRepository
                    .findFirstByStudentId(user.getId())
                    .map(e -> e.getBatch().getId())
                    .orElse(null);
        }

        // Step 7 — Get instituteId
        Long instituteId = user.getInstitute() != null
                ? user.getInstitute().getId() : null;

        // Step 8 — Build response
        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .userId(user.getId())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .redirectUrl(getRedirectUrl(user.getRole()))
                .batchId(batchId)
                .instituteId(instituteId)
                .build();
    }

    private String getRedirectUrl(Role role) {
        return switch (role) {
            case SUPER_ADMIN -> "/super-admin/dashboard";
            case ADMIN       -> "/admin/dashboard";
            case INSTRUCTOR  -> "/instructor/dashboard";
            case STUDENT     -> "/student/dashboard";
        };
    }
}