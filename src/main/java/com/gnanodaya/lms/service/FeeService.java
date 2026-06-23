package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.Fee;
import com.gnanodaya.lms.enums.FeeStatus;
import com.gnanodaya.lms.repository.FeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeeService {

    private final FeeRepository feeRepository;

    // Create fee record for a student
    public Fee createFee(Fee fee) {
        fee.setPendingAmount(fee.getTotalAmount());
        fee.setPaidAmount(BigDecimal.ZERO);
        fee.setStatus(FeeStatus.PENDING);
        return feeRepository.save(fee);
    }

    // Record a payment
    public Fee recordPayment(Long feeId, BigDecimal amount, String paymentMode, String transactionId) {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new RuntimeException("Fee record not found"));

        BigDecimal newPaid = fee.getPaidAmount().add(amount);
        BigDecimal newPending = fee.getTotalAmount().subtract(newPaid);

        fee.setPaidAmount(newPaid);
        fee.setPendingAmount(newPending.max(BigDecimal.ZERO));
        fee.setPaymentMode(paymentMode);
        fee.setTransactionId(transactionId);
        fee.setPaidDate(LocalDate.now());

        // Update status
        if (newPending.compareTo(BigDecimal.ZERO) <= 0) {
            fee.setStatus(FeeStatus.PAID);
        } else if (newPaid.compareTo(BigDecimal.ZERO) > 0) {
            fee.setStatus(FeeStatus.PARTIAL);
        }

        return feeRepository.save(fee);
    }

    public List<Fee> getFeesByStudent(Long studentId) {
        return feeRepository.findByStudentId(studentId);
    }

    public List<Fee> getFeesByBatch(Long batchId) {
        return feeRepository.findByBatchId(batchId);
    }

    public List<Fee> getOverdueFees() {
        return feeRepository.findByStatus(FeeStatus.OVERDUE);
    }

    public BigDecimal getTotalCollected(Long instituteId) {
        BigDecimal result = feeRepository.getTotalCollectedByInstitute(instituteId);
        return result != null ? result : BigDecimal.ZERO;
    }

    public BigDecimal getTotalPending(Long instituteId) {
        BigDecimal result = feeRepository.getTotalPendingByInstitute(instituteId);
        return result != null ? result : BigDecimal.ZERO;
    }
}