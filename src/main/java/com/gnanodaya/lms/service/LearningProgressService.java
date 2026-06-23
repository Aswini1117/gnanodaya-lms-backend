package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.LearningProgress;
import com.gnanodaya.lms.repository.CourseContentRepository;
import com.gnanodaya.lms.repository.LearningProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LearningProgressService {

    private final LearningProgressRepository progressRepository;
    private final CourseContentRepository contentRepository;

    public LearningProgress updateProgress(Long studentId, Long contentId, Integer watchedDuration) {
        LearningProgress progress = progressRepository
                .findByStudentIdAndContentId(studentId, contentId)
                .orElse(LearningProgress.builder()
                        .isCompleted(false)
                        .progressPercent(0)
                        .build());

        progress.setWatchedDuration(watchedDuration);

        if (progress.getTotalDuration() != null && progress.getTotalDuration() > 0) {
            int percent = (int) ((watchedDuration * 100.0) / progress.getTotalDuration());
            progress.setProgressPercent(Math.min(percent, 100));
            progress.setIsCompleted(percent >= 90);
        }

        return progressRepository.save(progress);
    }

    public List<LearningProgress> getStudentProgress(Long studentId, Long batchId) {
        return progressRepository.findByStudentIdAndBatchId(studentId, batchId);
    }

    public Map<String, Object> getBatchProgressSummary(Long studentId, Long batchId) {
        long totalContent = contentRepository.findByBatchIdOrderByOrderIndex(batchId).size();
        long completed = progressRepository.countCompletedByStudentAndBatch(studentId, batchId);
        double percent = totalContent > 0 ? (completed * 100.0 / totalContent) : 0;
        return Map.of(
                "totalContent", totalContent,
                "completed", completed,
                "remaining", totalContent - completed,
                "overallPercent", Math.round(percent * 10.0) / 10.0
        );
    }
}