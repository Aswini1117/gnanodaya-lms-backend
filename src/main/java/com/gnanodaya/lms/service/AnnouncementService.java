package com.gnanodaya.lms.service;

import com.gnanodaya.lms.dto.announcement.AnnouncementRequest;
import com.gnanodaya.lms.dto.announcement.AnnouncementResponse;
import com.gnanodaya.lms.entity.Announcement;
import com.gnanodaya.lms.entity.Batch;
import com.gnanodaya.lms.entity.Institute;
import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.repository.AnnouncementRepository;
import com.gnanodaya.lms.repository.BatchRepository;
import com.gnanodaya.lms.repository.InstituteRepository;
import com.gnanodaya.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final InstituteRepository instituteRepository;
    private final UserRepository userRepository;
    private final BatchRepository batchRepository;

    @Transactional
    public AnnouncementResponse createAnnouncement(AnnouncementRequest request) {

        Institute institute = instituteRepository.findById(request.getInstituteId())
                .orElseThrow(() -> new RuntimeException("Institute not found"));

        User createdBy = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Batch batch = null;
        if (request.getBatchId() != null) {
            batch = batchRepository.findById(request.getBatchId())
                    .orElseThrow(() -> new RuntimeException("Batch not found"));
        }

        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .targetType(request.getTargetType())
                .institute(institute)
                .createdBy(createdBy)
                .batch(batch)
                .build();

        Announcement saved = announcementRepository.save(announcement);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getByInstitute(Long instituteId) {
        return announcementRepository.findByInstituteId(instituteId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getByBatch(Long batchId) {
        return announcementRepository.findByBatchId(batchId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getByCreatedBy(Long userId) {
        return announcementRepository.findByCreatedById(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }

    private AnnouncementResponse mapToResponse(Announcement a) {
        AnnouncementResponse response = new AnnouncementResponse();
        response.setId(a.getId());
        response.setTitle(a.getTitle());
        response.setContent(a.getContent());
        response.setTargetType(a.getTargetType());
        response.setCreatedAt(a.getCreatedAt());
        return response;
    }
}