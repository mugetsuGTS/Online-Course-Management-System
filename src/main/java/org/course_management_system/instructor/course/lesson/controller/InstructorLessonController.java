package org.course_management_system.instructor.course.lesson.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.course_management_system.course.lesson.dto.LessonCreateOrUpdateDTO;
import org.course_management_system.course.lesson.dto.LessonDTO;
import org.course_management_system.course.lesson.service.LessonService;
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
@RequestMapping("instructor/courses/{courseId}/lessons")
public class InstructorLessonController {

    private final LessonService service;

    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public PageView<LessonDTO> getLessons(
            @Valid @NotNull PaginationUtil paginationUtil,
            @PathVariable Long courseId,
            @RequestParam(name = "search", required = false) String search
    ) {
        return service.getLessonsForInstructor(courseId, search, paginationUtil);
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createLesson(@PathVariable Long courseId,
                             @RequestBody @Valid @NotNull LessonCreateOrUpdateDTO dto) {
        service.createLesson(courseId, dto);
    }

    @PutMapping("{lessonId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLesson(@PathVariable Long courseId,
                             @PathVariable("lessonId") Long lessonId,
                             @RequestBody @Valid @NotNull LessonCreateOrUpdateDTO dto) {
        service.updateLesson(courseId, lessonId, dto);
    }

    @DeleteMapping("{lessonId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLesson(@PathVariable Long courseId,
                             @PathVariable("lessonId") Long lessonId) {
        service.removeLesson(courseId, lessonId);
    }
}
