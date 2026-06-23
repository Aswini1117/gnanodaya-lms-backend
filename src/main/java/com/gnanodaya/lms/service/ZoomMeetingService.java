package com.gnanodaya.lms.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gnanodaya.lms.dto.zoom.ZoomMeetingRequest;
import com.gnanodaya.lms.dto.zoom.ZoomMeetingResponse;
import com.gnanodaya.lms.entity.Batch;
import com.gnanodaya.lms.entity.Institute;
import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.entity.ZoomMeeting;
import com.gnanodaya.lms.enums.Role;
import com.gnanodaya.lms.enums.ZoomMeetingStatus;
import com.gnanodaya.lms.enums.ZoomTargetType;
import com.gnanodaya.lms.repository.BatchRepository;
import com.gnanodaya.lms.repository.InstituteRepository;
import com.gnanodaya.lms.repository.UserRepository;
import com.gnanodaya.lms.repository.ZoomMeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoomMeetingService {

    private final ZoomMeetingRepository zoomMeetingRepository;
    private final ZoomAuthService zoomAuthService;
    private final BatchRepository batchRepository;
    private final UserRepository userRepository;
    private final InstituteRepository instituteRepository;
    private final ObjectMapper objectMapper;

    @Value("${zoom.api.base.url}")
    private String zoomApiBaseUrl;

    // ── CREATE MEETING ────────────────────────────────

    @Transactional
    public ZoomMeetingResponse createMeeting(
            ZoomMeetingRequest request, Long creatorId) {

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ZoomTargetType targetType = request.getTargetType() != null
                ? request.getTargetType()
                : ZoomTargetType.BOTH;

        validateTargetTypeForRole(creator.getRole(), targetType);

        Batch batch = null;
        Set<User> invitees = new HashSet<>();

        boolean needsBatch = targetType == ZoomTargetType.STUDENTS
                || targetType == ZoomTargetType.BOTH;

        if (needsBatch) {
            if (request.getBatchId() == null) {
                throw new RuntimeException(
                        "Please select a batch for this session type.");
            }
            batch = batchRepository.findById(request.getBatchId())
                    .orElseThrow(() -> new RuntimeException("Batch not found"));
        } else {
            if (request.getInvitedUserIds() == null
                    || request.getInvitedUserIds().isEmpty()) {
                throw new RuntimeException(
                        "Please select at least one recipient for this session type.");
            }
            invitees = new HashSet<>(
                    userRepository.findAllById(request.getInvitedUserIds()));

            if (targetType == ZoomTargetType.INSTRUCTORS) {
                boolean allInstructors = invitees.stream()
                        .allMatch(u -> u.getRole() == Role.INSTRUCTOR);
                if (!allInstructors) {
                    throw new RuntimeException(
                            "Only instructors can be selected for this session type.");
                }
            }
            if (targetType == ZoomTargetType.STAFF) {
                boolean allStaff = invitees.stream()
                        .allMatch(u -> u.getRole() == Role.INSTRUCTOR
                                || u.getRole() == Role.ADMIN);
                if (!allStaff) {
                    throw new RuntimeException(
                            "Only instructors and admins can be selected for this session type.");
                }
            }
        }

        Institute institute = creator.getInstitute();
        if (institute == null) {
            if (request.getInstituteId() == null) {
                throw new RuntimeException(
                        "Institute is required for this session.");
            }
            institute = instituteRepository.findById(request.getInstituteId())
                    .orElseThrow(() -> new RuntimeException("Institute not found"));
        }

        try {
            String accessToken = zoomAuthService.getAccessToken();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            ObjectNode meetingBody = objectMapper.createObjectNode();
            meetingBody.put("topic", request.getTopic());
            meetingBody.put("type", 2);
            meetingBody.put("start_time",
                    request.getStartTime().format(
                            DateTimeFormatter.ofPattern(
                                    "yyyy-MM-dd'T'HH:mm:ss")));
            meetingBody.put("duration", request.getDuration());
            meetingBody.put("timezone", "Asia/Kolkata");

            if (request.getAgenda() != null) {
                meetingBody.put("agenda", request.getAgenda());
            }

            ObjectNode settings = objectMapper.createObjectNode();
            settings.put("host_video", true);
            settings.put("participant_video", true);
            settings.put("join_before_host", false);
            settings.put("waiting_room", true);
            settings.put("auto_recording", "none");
            meetingBody.set("settings", settings);

            HttpEntity<String> httpRequest = new HttpEntity<>(
                    meetingBody.toString(), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    zoomApiBaseUrl + "/users/me/meetings",
                    httpRequest,
                    String.class);

            if (response.getStatusCode() == HttpStatus.CREATED
                    && response.getBody() != null) {

                JsonNode zoomResponse = objectMapper.readTree(
                        response.getBody());

                ZoomMeeting meeting = ZoomMeeting.builder()
                        .zoomMeetingId(zoomResponse.get("id").asText())
                        .topic(request.getTopic())
                        .agenda(request.getAgenda())
                        .startTime(request.getStartTime())
                        .duration(request.getDuration())
                        .joinUrl(zoomResponse.get("join_url").asText())
                        .startUrl(zoomResponse.get("start_url").asText())
                        .meetingPassword(zoomResponse.get("password").asText())
                        .status(ZoomMeetingStatus.SCHEDULED)
                        .instructor(creator)
                        .batch(batch)
                        .institute(institute)
                        .targetType(targetType)
                        .invitees(invitees)
                        .build();

                ZoomMeeting saved = zoomMeetingRepository.save(meeting);
                log.info("Zoom meeting created: {} ({}) by user {}",
                        saved.getZoomMeetingId(), targetType, creatorId);

                return mapToResponse(saved, batch, creator);
            }

        } catch (Exception e) {
            log.error("Failed to create Zoom meeting: {}", e.getMessage());
            throw new RuntimeException(
                    "Failed to create Zoom meeting: " + e.getMessage());
        }

        throw new RuntimeException("Failed to create Zoom meeting");
    }

    private void validateTargetTypeForRole(Role role, ZoomTargetType targetType) {
        if (role == Role.SUPER_ADMIN && targetType != ZoomTargetType.STAFF) {
            throw new RuntimeException(
                    "Super Admin can only create sessions for instructors and admins.");
        }
        if (role == Role.ADMIN && targetType == ZoomTargetType.STAFF) {
            throw new RuntimeException(
                    "Admin cannot create staff-only sessions.");
        }
        if (role == Role.INSTRUCTOR
                && targetType != ZoomTargetType.BOTH
                && targetType != ZoomTargetType.STUDENTS) {
            throw new RuntimeException(
                    "Instructors can only schedule sessions for their batch.");
        }
    }

    // ── DELETE MEETING ────────────────────────────────

    @Transactional
    public void deleteMeeting(Long meetingId, Long userId) {

        ZoomMeeting meeting = zoomMeetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        if (!meeting.getInstructor().getId().equals(userId)) {
            throw new RuntimeException(
                    "You are not authorized to delete this meeting");
        }

        try {
            String accessToken = zoomAuthService.getAccessToken();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            restTemplate.exchange(
                    zoomApiBaseUrl + "/meetings/" + meeting.getZoomMeetingId(),
                    HttpMethod.DELETE,
                    request,
                    Void.class);

            meeting.setStatus(ZoomMeetingStatus.CANCELLED);
            zoomMeetingRepository.save(meeting);

            log.info("Zoom meeting deleted: {}", meeting.getZoomMeetingId());

        } catch (Exception e) {
            log.error("Failed to delete Zoom meeting: {}", e.getMessage());
            throw new RuntimeException(
                    "Failed to delete Zoom meeting: " + e.getMessage());
        }
    }

    // ── GET MEETINGS BY INSTRUCTOR (legacy, creator-only) ─

    @Transactional(readOnly = true)
    public List<ZoomMeetingResponse> getMeetingsByInstructor(Long instructorId) {
        return zoomMeetingRepository
                .findByInstructorIdOrderByStartTimeDesc(instructorId)
                .stream()
                .map(m -> mapToResponse(m, m.getBatch(), m.getInstructor()))
                .collect(Collectors.toList());
    }

    // ── GET MEETINGS FOR A USER (creator OR invitee) ──────

    @Transactional(readOnly = true)
    public List<ZoomMeetingResponse> getMeetingsForUser(Long userId) {
        List<ZoomMeeting> hosted = zoomMeetingRepository
                .findByInstructorIdOrderByStartTimeDesc(userId);
        List<ZoomMeeting> invited = zoomMeetingRepository
                .findByInviteesIdOrderByStartTimeDesc(userId);

        Map<Long, ZoomMeeting> merged = new LinkedHashMap<>();
        for (ZoomMeeting m : hosted) merged.put(m.getId(), m);
        for (ZoomMeeting m : invited) merged.putIfAbsent(m.getId(), m);

        return merged.values().stream()
                .sorted(Comparator.comparing(ZoomMeeting::getStartTime).reversed())
                .map(m -> mapToResponse(m, m.getBatch(), m.getInstructor()))
                .collect(Collectors.toList());
    }

    // ── GET MEETINGS BY BATCH ─────────────────────────

    @Transactional(readOnly = true)
    public List<ZoomMeetingResponse> getMeetingsByBatch(Long batchId) {
        return zoomMeetingRepository
                .findByBatchIdOrderByStartTimeDesc(batchId)
                .stream()
                .map(m -> mapToResponse(m, m.getBatch(), m.getInstructor()))
                .collect(Collectors.toList());
    }

    // ── GET ALL MEETINGS BY INSTITUTE ─────────────────

    @Transactional(readOnly = true)
    public List<ZoomMeetingResponse> getMeetingsByInstitute(Long instituteId) {
        return zoomMeetingRepository
                .findByInstituteIdOrderByStartTimeDesc(instituteId)
                .stream()
                .map(m -> mapToResponse(m, m.getBatch(), m.getInstructor()))
                .collect(Collectors.toList());
    }

    // ── GET SINGLE MEETING ────────────────────────────

    @Transactional(readOnly = true)
    public ZoomMeetingResponse getMeetingById(Long meetingId) {
        ZoomMeeting meeting = zoomMeetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
        return mapToResponse(meeting, meeting.getBatch(), meeting.getInstructor());
    }

    // ── UPDATE MEETING STATUS ─────────────────────────

    @Transactional
    public ZoomMeetingResponse updateMeetingStatus(
            Long meetingId, ZoomMeetingStatus status) {
        ZoomMeeting meeting = zoomMeetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
        meeting.setStatus(status);
        ZoomMeeting saved = zoomMeetingRepository.save(meeting);
        return mapToResponse(saved, saved.getBatch(), saved.getInstructor());
    }

    // ── MAP TO RESPONSE ───────────────────────────────

    private ZoomMeetingResponse mapToResponse(
            ZoomMeeting meeting, Batch batch, User host) {

        ZoomMeetingResponse response = new ZoomMeetingResponse();
        response.setId(meeting.getId());
        response.setZoomMeetingId(meeting.getZoomMeetingId());
        response.setTopic(meeting.getTopic());
        response.setAgenda(meeting.getAgenda());
        response.setStartTime(meeting.getStartTime());
        response.setDuration(meeting.getDuration());
        response.setJoinUrl(meeting.getJoinUrl());
        response.setStartUrl(meeting.getStartUrl());
        response.setMeetingPassword(meeting.getMeetingPassword());
        response.setStatus(meeting.getStatus());
        response.setTargetType(meeting.getTargetType());
        response.setInstructorName(host != null ? host.getFullName() : "N/A");

        if (batch != null) {
            response.setBatchName(batch.getBatchName());
            try {
                response.setCourseName(
                        batch.getCourse() != null ? batch.getCourse().getTitle() : "N/A");
            } catch (Exception e) {
                response.setCourseName("N/A");
            }
        } else {
            response.setBatchName("N/A");
            response.setCourseName("N/A");
        }

        List<String> inviteeNames = meeting.getInvitees() == null
                ? Collections.emptyList()
                : meeting.getInvitees().stream()
                .map(User::getFullName)
                .collect(Collectors.toList());
        response.setInviteeNames(inviteeNames);

        return response;
    }
}