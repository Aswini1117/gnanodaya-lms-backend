package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.Enquiry;
import com.gnanodaya.lms.repository.EnquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;

    public Enquiry createEnquiry(Enquiry enquiry) {
        enquiry.setStatus("PENDING");
        return enquiryRepository.save(enquiry);
    }

    public List<Enquiry> getEnquiriesByInstitute(Long instituteId) {
        return enquiryRepository.findByInstituteId(instituteId);
    }

    public Enquiry updateEnquiryStatus(Long id, String status, String followUpNote) {
        Enquiry enquiry = enquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enquiry not found"));
        enquiry.setStatus(status);
        if (followUpNote != null) enquiry.setFollowUpNote(followUpNote);
        return enquiryRepository.save(enquiry);
    }

    public void deleteEnquiry(Long id) {
        enquiryRepository.deleteById(id);
    }
}