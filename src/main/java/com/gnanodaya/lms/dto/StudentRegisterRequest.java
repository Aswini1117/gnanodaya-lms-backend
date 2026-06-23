package com.gnanodaya.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentRegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    private String phone;

    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;
}