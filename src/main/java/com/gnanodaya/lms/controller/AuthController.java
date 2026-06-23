package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.request.StudentRegisterRequest;
import com.gnanodaya.lms.dto.request.CreateUserRequest;
import com.gnanodaya.lms.dto.request.LoginRequest;
import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.dto.response.AuthResponse;
import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.enums.Role;
import com.gnanodaya.lms.enums.UserStatus;
import com.gnanodaya.lms.repository.UserRepository;
import com.gnanodaya.lms.service.AuthService;
import com.gnanodaya.lms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and Student Registration APIs")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ── 1. PING ────────────────────────────────────────────
    @GetMapping("/ping")
    @Operation(
            summary = "Health Check",
            description = "Check if backend is running. No token needed."
    )
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("LMS Backend is running!");
    }

    // ── 2. SETUP SUPER ADMIN ───────────────────────────────
    @PostMapping("/setup")
    @Operation(
            summary = "Create Super Admin (Run Only Once)",
            description = "Creates the first Super Admin. Only works if no Super Admin exists."
    )
    public ResponseEntity<ApiResponse<User>> setup(
            @RequestBody CreateUserRequest request) {
        try {
            if (userRepository.existsByRole(Role.SUPER_ADMIN)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Super Admin already exists"));
            }
            request.setRole("SUPER_ADMIN");
            User user = userService.createUser(request);
            return ResponseEntity.ok(
                    ApiResponse.success("Super Admin created successfully", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // ── 3. LOGIN ───────────────────────────────────────────
    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = """
            Login with phone + password + role. Returns JWT token.
            
            Roles: SUPER_ADMIN, ADMIN, INSTRUCTOR, STUDENT
            
            Note: Students must be ACTIVE (approved by Admin) to login.
            Students with PENDING status cannot login.
            """
    )
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(
                    ApiResponse.success("Login successful", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // ── 4. STUDENT REGISTER ────────────────────────────────
    @PostMapping("/register")
    @Operation(
            summary = "Student Self Registration",
            description = """
            Only for Students. No OTP needed.
            
            After registration:
            - Account is created with status = PENDING
            - Student CANNOT login yet
            - Admin must approve (Give Access) first
            - Once Admin approves → status = ACTIVE → Student can login
            
            Fields required: fullName, phone, email, password, confirmPassword
            """
    )
    public ResponseEntity<ApiResponse<?>> register(
            @RequestBody StudentRegisterRequest request) {
        try {
            // 1. Check passwords match
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Passwords do not match."));
            }

            // 2. Check phone not already registered
            if (userRepository.existsByPhone(request.getPhone())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(
                                "Phone number already registered. Please sign in."));
            }

            // 3. Check email not already registered
            if (request.getEmail() != null &&
                    userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(
                                "Email already registered. Please sign in."));
            }

            // 4. Create Student with PENDING status
            // Admin must approve before student can login
            User student = User.builder()
                    .fullName(request.getFullName())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.STUDENT)
                    .status(UserStatus.PENDING)  // ← PENDING until Admin approves
                    .institute(null)             // ← Admin will assign institute
                    .build();

            User savedStudent = userRepository.save(student);

            return ResponseEntity.ok(ApiResponse.success(
                    "Registration successful! Please wait for Admin approval before signing in.",
                    java.util.Map.of(
                            "id",       savedStudent.getId(),
                            "fullName", savedStudent.getFullName(),
                            "phone",    savedStudent.getPhone(),
                            "status",   savedStudent.getStatus(),
                            "message",  "Your account is pending approval. Admin will activate your account soon."
                    )
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}