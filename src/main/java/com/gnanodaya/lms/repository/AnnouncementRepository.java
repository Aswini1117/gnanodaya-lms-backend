package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByInstituteId(Long instituteId);
    List<Announcement> findByBatchId(Long batchId);
    List<Announcement> findByCreatedById(Long userId);
}