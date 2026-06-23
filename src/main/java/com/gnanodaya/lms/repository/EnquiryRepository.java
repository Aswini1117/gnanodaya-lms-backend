package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    List<Enquiry> findByInstituteId(Long instituteId);
    List<Enquiry> findByInstituteIdAndStatus(Long instituteId, String status);
    List<Enquiry> findByAssignedToId(Long userId);
}