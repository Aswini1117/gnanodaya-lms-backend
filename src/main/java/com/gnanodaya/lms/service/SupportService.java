package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.SupportTicket;
import com.gnanodaya.lms.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportTicketRepository supportTicketRepository;

    public SupportTicket raiseTicket(SupportTicket ticket) {
        ticket.setStatus("OPEN");
        if (ticket.getPriority() == null) ticket.setPriority("MEDIUM");
        return supportTicketRepository.save(ticket);
    }

    public List<SupportTicket> getMyTickets(Long userId) {
        return supportTicketRepository.findByRaisedById(userId);
    }

    public List<SupportTicket> getTicketsByInstitute(Long instituteId) {
        return supportTicketRepository.findByInstituteId(instituteId);
    }

    public SupportTicket replyToTicket(Long ticketId, String reply, String status) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setAdminReply(reply);
        ticket.setStatus(status);
        return supportTicketRepository.save(ticket);
    }

    public SupportTicket updateStatus(Long ticketId, String status) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setStatus(status);
        return supportTicketRepository.save(ticket);
    }
}