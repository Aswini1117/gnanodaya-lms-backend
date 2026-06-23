package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.Course;
import com.gnanodaya.lms.entity.CourseContent;
import com.gnanodaya.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // ─── COURSES ─────────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(ApiResponse.success("Course created", courseService.createCourse(course)));
    }

    @GetMapping("/institute/{instituteId}")
    public ResponseEntity<ApiResponse<List<Course>>> getCoursesByInstitute(@PathVariable Long instituteId) {
        return ResponseEntity.ok(ApiResponse.success("Courses", courseService.getCoursesByInstitute(instituteId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Course", courseService.getCourseById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Course>> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        return ResponseEntity.ok(ApiResponse.success("Course updated", courseService.updateCourse(id, course)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course deleted", null));
    }

    // ─── COURSE CONTENT ──────────────────────────────────────────────

    @PostMapping("/content")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CourseContent>> addContent(@RequestBody CourseContent content) {
        return ResponseEntity.ok(ApiResponse.success("Content added", courseService.addContent(content)));
    }

    @GetMapping("/{courseId}/content")
    public ResponseEntity<ApiResponse<List<CourseContent>>> getContent(@PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.success("Course content", courseService.getContentByCourse(courseId)));
    }

    @GetMapping("/batch/{batchId}/content")
    public ResponseEntity<ApiResponse<List<CourseContent>>> getBatchContent(@PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Batch content", courseService.getContentByBatch(batchId)));
    }

    @DeleteMapping("/content/{contentId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteContent(@PathVariable Long contentId) {
        courseService.deleteContent(contentId);
        return ResponseEntity.ok(ApiResponse.success("Content deleted", null));
    }
}