package com.gnanodaya.lms.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    // Step 1 - verified phone
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit phone number")
    private String phone;

    // Step 3 - institute details
    @NotBlank(message = "Institute name is required")
    private String instituteName;

    private String portalUrl; // optional - auto-generated if empty

    @NotBlank(message = "Your name is required")
    private String adminName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    private String address;
}