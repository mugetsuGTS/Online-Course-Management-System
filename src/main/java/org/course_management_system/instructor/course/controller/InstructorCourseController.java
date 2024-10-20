package org.course_management_system.instructor.course.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.course_management_system.course.dto.CourseCreateOrUpdateDTO;
import org.course_management_system.course.dto.CourseDTO;
import org.course_management_system.course.service.CourseService;
import org.course_management_system.util.PageView;
import org.course_management_system.util.PaginationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("instructor/courses")
public class InstructorCourseController {

    private final CourseService service;

    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public PageView<CourseDTO> getCourses(
            @Valid @NotNull PaginationUtil paginationUtil,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "categoryId", required = false) Long categoryId
    ) {
        return service.getCoursesForInstructor(paginationUtil, search, categoryId);
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCourse(@RequestBody @Valid @NotNull CourseCreateOrUpdateDTO dto) {
        service.createCourse(dto);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCourse(
            @PathVariable @NotNull Long id,
            @RequestBody @Valid @NotNull CourseCreateOrUpdateDTO dto
    ) {
        service.updateCourse(id, dto);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable @NotNull Long id) {
        service.deleteCourse(id);
    }

}
