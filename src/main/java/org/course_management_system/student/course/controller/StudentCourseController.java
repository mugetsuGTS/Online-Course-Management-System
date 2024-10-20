package org.course_management_system.student.course.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.course_management_system.category.controller.dto.CategoryDTO;
import org.course_management_system.course.dto.CourseDTO;
import org.course_management_system.course.dto.ReviewDTO;
import org.course_management_system.course.dto.StudentCourseDTO;
import org.course_management_system.course.repository.enums.Difficulty;
import org.course_management_system.course.service.CourseService;
import org.course_management_system.student.enrollment.service.EnrollmentService;
import org.course_management_system.util.PageView;
import org.course_management_system.util.PaginationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("student/courses")
public class StudentCourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public PageView<CourseDTO> getAllCourses(
            @Valid @NotNull PaginationUtil paginationUtil,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "difficultyLevel", required = false) Difficulty difficulty,
            @RequestParam(name = "durationFrom", required = false) Duration durationFrom,
            @RequestParam(name = "durationTo", required = false) Duration durationTo
    ) {
        return courseService.getCoursesForStudents(search, difficulty, durationFrom, durationTo, paginationUtil);
    }

    @GetMapping("{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public StudentCourseDTO getCourseById(@PathVariable Long courseId) {
        return enrollmentService.getCourseById(courseId);
    }

    @PatchMapping("enroll/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enrollInCourse(@PathVariable Long courseId) {
        enrollmentService.enrollCurrentStudentOnCourse(courseId);
    }

    @PatchMapping("leave/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveEnrolledCourse(@PathVariable Long courseId) {
        enrollmentService.removeCurrentStudentEnrollmentOnCourse(courseId);
    }

    @GetMapping("enrolled")
    @PreAuthorize("hasRole('STUDENT')")
    public PageView<CourseDTO> getAllCoursesWhereStudentIsEnrolled(
            @Valid @NotNull PaginationUtil paginationUtil,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "difficultyLevel", required = false) Difficulty difficulty,
            @RequestParam(name = "durationFrom", required = false) Duration durationFrom,
            @RequestParam(name = "durationTo", required = false) Duration durationTo
    ) {
        return enrollmentService.getEnrolledCourses(search, difficulty, durationFrom, durationTo, paginationUtil);
    }

    @GetMapping("enrolled/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public StudentCourseDTO getEnrolledCourseById(@PathVariable Long courseId) {
        return enrollmentService.getEnrolledCourseById(courseId);
    }

    @GetMapping("enrolled/{courseId}/review")
    @PreAuthorize("hasRole('STUDENT')")
    public void reviewCourseWhereStudentIsEnrolled(
            @PathVariable Long courseId,
            @RequestBody @Valid @NotNull ReviewDTO reviewDTO
    ) {
        enrollmentService.reviewEnrolledAndCompletedCourseById(courseId, reviewDTO);
    }
}
