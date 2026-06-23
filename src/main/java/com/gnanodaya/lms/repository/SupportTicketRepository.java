package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByRaisedById(Long userId);
    List<SupportTicket> findByInstituteId(Long instituteId);
    List<SupportTicket> findByInstituteIdAndStatus(Long instituteId, String status);
    List<SupportTicket> findByAssignedToId(Long adminId);
}