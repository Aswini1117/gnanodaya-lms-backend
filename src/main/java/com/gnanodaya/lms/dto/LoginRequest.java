package com.gnanodaya.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Role is required")
    private String role; // STUDENT, INSTRUCTOR, ADMIN, SUPER_ADMIN
}