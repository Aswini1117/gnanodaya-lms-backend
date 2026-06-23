package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.Fee;
import com.gnanodaya.lms.service.FeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fees")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Fee>> createFee(@RequestBody Fee fee) {
        return ResponseEntity.ok(ApiResponse.success("Fee record created",
                feeService.createFee(fee)));
    }

    @PostMapping("/{feeId}/pay")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Fee>> recordPayment(
            @PathVariable Long feeId,
            @RequestParam BigDecimal amount,
            @RequestParam String paymentMode,
            @RequestParam(required = false) String transactionId) {
        return ResponseEntity.ok(ApiResponse.success("Payment recorded",
                feeService.recordPayment(feeId, amount, paymentMode, transactionId)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Fee>>> getFeesByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("Student fees",
                feeService.getFeesByStudent(studentId)));
    }

    @GetMapping("/batch/{batchId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<Fee>>> getFeesByBatch(
            @PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Batch fees",
                feeService.getFeesByBatch(batchId)));
    }

    @GetMapping("/summary/{instituteId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFeeSummary(
            @PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Fee summary",
                Map.of(
                        "totalCollected", feeService.getTotalCollected(instituteId),
                        "totalPending", feeService.getTotalPending(instituteId)
                )));
    }
}