package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.DemoClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DemoClassRepository extends JpaRepository<DemoClass, Long> {
    List<DemoClass> findByInstituteId(Long instituteId);
    List<DemoClass> findByInstructorId(Long instructorId);
    List<DemoClass> findByInstituteIdAndStatus(Long instituteId, String status);
}