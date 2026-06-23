package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.Enquiry;
import com.gnanodaya.lms.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/enquiries")
@RequiredArgsConstructor
public class EnquiryController {

    private final EnquiryService enquiryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Enquiry>> createEnquiry(
            @RequestBody Enquiry enquiry) {
        return ResponseEntity.ok(ApiResponse.success("Enquiry submitted",
                enquiryService.createEnquiry(enquiry)));
    }

    @GetMapping("/institute/{instituteId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<Enquiry>>> getEnquiries(
            @PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Enquiries",
                enquiryService.getEnquiriesByInstitute(instituteId)));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Enquiry>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String followUpNote) {
        return ResponseEntity.ok(ApiResponse.success("Status updated",
                enquiryService.updateEnquiryStatus(id, status, followUpNote)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEnquiry(@PathVariable Long id) {
        enquiryService.deleteEnquiry(id);
        return ResponseEntity.ok(ApiResponse.success("Enquiry deleted", null));
    }
}
