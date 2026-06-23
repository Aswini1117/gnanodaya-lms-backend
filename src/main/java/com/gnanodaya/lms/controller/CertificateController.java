package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.Certificate;
import com.gnanodaya.lms.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateRepository certificateRepository;

    @PostMapping("/issue")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Certificate>> issueCertificate(
            @RequestBody Certificate certificate) {
        certificate.setCertificateNumber("GVLSI-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        certificate.setIssuedDate(LocalDate.now());
        certificate.setStatus("ISSUED");
        return ResponseEntity.ok(ApiResponse.success("Certificate issued",
                certificateRepository.save(certificate)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Certificate>>> getStudentCertificates(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("Certificates",
                certificateRepository.findByStudentId(studentId)));
    }

    @GetMapping("/verify/{certificateNumber}")
    public ResponseEntity<ApiResponse<Certificate>> verifyCertificate(
            @PathVariable String certificateNumber) {
        Certificate cert = certificateRepository
                .findByCertificateNumber(certificateNumber)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
        return ResponseEntity.ok(ApiResponse.success("Certificate is valid", cert));
    }

    @GetMapping("/institute/{instituteId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<Certificate>>> getInstituteCertificates(
            @PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Certificates",
                certificateRepository.findByInstituteId(instituteId)));
    }
}