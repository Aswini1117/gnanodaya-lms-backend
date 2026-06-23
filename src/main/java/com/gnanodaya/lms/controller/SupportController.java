package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.SupportTicket;
import com.gnanodaya.lms.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportService;

    // Student or Instructor raises a ticket
    @PostMapping
    public ResponseEntity<ApiResponse<SupportTicket>> raiseTicket(@RequestBody SupportTicket ticket) {
        return ResponseEntity.ok(ApiResponse.success("Ticket raised", supportService.raiseTicket(ticket)));
    }

    // Get my own tickets
    @GetMapping("/my/{userId}")
    public ResponseEntity<ApiResponse<List<SupportTicket>>> getMyTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("My tickets", supportService.getMyTickets(userId)));
    }

    // Admin views all institute tickets
    @GetMapping("/institute/{instituteId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<SupportTicket>>> getInstituteTickets(@PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("All tickets", supportService.getTicketsByInstitute(instituteId)));
    }

    // Admin replies to a ticket
    @PutMapping("/{ticketId}/reply")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<SupportTicket>> replyToTicket(
            @PathVariable Long ticketId,
            @RequestParam String reply,
            @RequestParam(defaultValue = "RESOLVED") String status) {
        return ResponseEntity.ok(ApiResponse.success("Reply sent",
                supportService.replyToTicket(ticketId, reply, status)));
    }

    // Update ticket status
    @PutMapping("/{ticketId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<SupportTicket>> updateStatus(
            @PathVariable Long ticketId, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("Status updated",
                supportService.updateStatus(ticketId, status)));
    }
}