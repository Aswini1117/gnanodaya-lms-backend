package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.Course;
import com.gnanodaya.lms.entity.CourseContent;
import com.gnanodaya.lms.enums.CourseStatus;
import com.gnanodaya.lms.repository.CourseContentRepository;
import com.gnanodaya.lms.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseContentRepository courseContentRepository;

    public Course createCourse(Course course) {
        course.setStatus(CourseStatus.ACTIVE);
        return courseRepository.save(course);
    }

    public List<Course> getCoursesByInstitute(Long instituteId) {
        return courseRepository.findByInstituteId(instituteId);
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course updateCourse(Long id, Course updated) {
        Course course = getCourseById(id);
        course.setTitle(updated.getTitle());
        course.setDescription(updated.getDescription());
        course.setFees(updated.getFees());
        course.setDurationWeeks(updated.getDurationWeeks());
        if (updated.getStatus() != null) course.setStatus(updated.getStatus());
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public CourseContent addContent(CourseContent content) {
        return courseContentRepository.save(content);
    }

    public List<CourseContent> getContentByCourse(Long courseId) {
        return courseContentRepository.findByCourseIdOrderByOrderIndex(courseId);
    }

    public List<CourseContent> getContentByBatch(Long batchId) {
        return courseContentRepository.findByBatchIdOrderByOrderIndex(batchId);
    }

    public void deleteContent(Long contentId) {
        courseContentRepository.deleteById(contentId);
    }
}