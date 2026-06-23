package com.gnanodaya.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;

    private Long instituteId;

    private String specialization;
}