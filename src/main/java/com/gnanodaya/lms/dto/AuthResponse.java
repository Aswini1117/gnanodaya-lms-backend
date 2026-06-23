package com.gnanodaya.lms.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private Long userId;
    private String fullName;
    private String phone;
    private String redirectUrl;
    private Long batchId;        // ← ADD THIS
    private Long instituteId;    // ← ADD THIS (useful for admin/super-admin)
}