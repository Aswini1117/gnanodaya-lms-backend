package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.CodingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CodingQuestionRepository extends JpaRepository<CodingQuestion, Long> {
    List<CodingQuestion> findByInstituteId(Long instituteId);
    List<CodingQuestion> findByInstituteIdAndStatus(Long instituteId, String status);
    List<CodingQuestion> findByInstituteIdAndDifficulty(Long instituteId, String difficulty);
    List<CodingQuestion> findByInstituteIdAndTopic(Long instituteId, String topic);
}