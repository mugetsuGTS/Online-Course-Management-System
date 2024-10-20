package org.course_management_system.student.course.lesson.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.course_management_system.course.lesson.dto.LessonDTO;
import org.course_management_system.course.lesson.dto.StudentLessonDTO;
import org.course_management_system.course.lesson.service.LessonService;
import org.course_management_system.util.PageView;
import org.course_management_system.util.PaginationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("student/courses/enrolled/{courseId}/lessons")
public class StudentLessonController {

    public final LessonService service;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public PageView<StudentLessonDTO> getLessons(
            @Valid @NotNull PaginationUtil paginationUtil,
            @PathVariable Long courseId,
            @RequestParam(name = "search", required = false) String search
    ) {
        return service.getLessonsForStudent(courseId, search, paginationUtil);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public LessonDTO getLessonById(@PathVariable Long courseId, @PathVariable Long id) {
        return service.getLessonById(courseId, id);
    }

    @GetMapping("{id}/complete")
    @PreAuthorize("hasRole('STUDENT')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void completeLessonById(@PathVariable Long courseId, @PathVariable Long id) {
        service.completeLessonById(courseId, id);
    }
}
