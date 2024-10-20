package org.course_management_system.course.lesson.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.course_management_system.course.lesson.dto.LessonCreateOrUpdateDTO;
import org.course_management_system.course.lesson.dto.LessonDTO;
import org.course_management_system.course.lesson.dto.StudentLessonDTO;
import org.course_management_system.course.lesson.repository.LessonRepository;
import org.course_management_system.course.lesson.repository.entity.Lesson;
import org.course_management_system.course.repository.entity.Course;
import org.course_management_system.course.service.CourseService;
import org.course_management_system.exception.BusinessException;
import org.course_management_system.exception.SecurityViolationException;
import org.course_management_system.exception.util.ExceptionUtil;
import org.course_management_system.student.enrollment.repository.entity.Enrollment;
import org.course_management_system.student.enrollment.service.EnrollmentService;
import org.course_management_system.util.PageView;
import org.course_management_system.util.PaginationUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonService {
    private final CourseService courseService;
    private final LessonRepository repository;
    private final EnrollmentService enrollmentService;

    public LessonDTO getLessonById(Long courseId, Long id) {
        return repository.findByIdAndCourse(id, courseService.lookUpCourse(courseId))
                .orElseThrow(SecurityViolationException::new);
    }

    public void createLesson(Long courseId, LessonCreateOrUpdateDTO dto) {
        Course course = courseService.lookUpCourseAndCheckInstructorAccess(courseId);
        Lesson lesson = Lesson.builder()
                .title(dto.title())
                .content(dto.content())
                .shortDescription(dto.shortDescription())
                .build();
        course.addLesson(lesson);
        courseService.justSaveCourse(course);
    }

    public void updateLesson(Long courseId, Long lessonId, LessonCreateOrUpdateDTO dto) {
        Course course = courseService.lookUpCourseAndCheckInstructorAccess(courseId);
        Lesson lesson = course.getLessons().stream().filter(l -> l.getId().equals(lessonId))
                .findFirst().orElseThrow(SecurityViolationException::new);

        lesson.setTitle(dto.title());
        lesson.setContent(dto.content());
        lesson.setShortDescription(dto.shortDescription());

        courseService.justSaveCourse(course);
    }

    public void removeLesson(Long courseId, Long lessonId) {
        Course course = courseService.lookUpCourseAndCheckInstructorAccess(courseId);
        Lesson lesson = course.getLessons().stream().filter(l -> l.getId().equals(lessonId))
                .findFirst().orElseThrow(SecurityViolationException::new);
        course.removeLesson(lesson);
        try {
            courseService.justSaveCourse(course);
        } catch (DataIntegrityViolationException e) {
            ExceptionUtil.handleDatabaseExceptions(e,
                    Map.of("update or delete on table \"lesson\" violates foreign key constraint", "can't delete lesson!"));
        }
    }

    public PageView<LessonDTO> getLessonsForInstructor(Long courseId, String search, @Valid @NotNull PaginationUtil paginationUtil) {
        Course course = courseService.lookUpCourseAndCheckInstructorAccess(courseId);
        return PageView.of(repository.getAllByCourseAndSearch(course, search, paginationUtil.build("id:desc")));
    }

    public PageView<StudentLessonDTO> getLessonsForStudent(Long courseId, String search, PaginationUtil paginationUtil) {
        Enrollment enrollment = enrollmentService.lookUpEnrolment(courseId);
        Set<Long> completedLessonIds;
        if (enrollment.isCourseCompleted()) {
            completedLessonIds = new HashSet<>();
        } else {
            completedLessonIds = enrollment.getCompletedLessons().stream()
                    .map(Lesson::getId)
                    .collect(Collectors.toSet());
        }

        Pageable pageable = paginationUtil.build("id:desc");
        Page<LessonDTO> lessons = repository.getAllByCourseAndSearch(enrollment.getCourse(), search, pageable);
        return PageView.of(
                new PageImpl<>(
                        lessons.stream().map(
                                l -> StudentLessonDTO.builder()
                                        .id(l.getId())
                                        .title(l.getTitle())
                                        .shortDescription(l.getShortDescription())
                                        .completed(enrollment.isCourseCompleted() || completedLessonIds.contains(l.getId()))
                                        .build()
                        ).toList(),
                        pageable,
                        lessons.getTotalElements()
                )
        );
    }

    public void completeLessonById(Long courseId, Long id) {
        Enrollment enrollment = enrollmentService.lookUpEnrolment(courseId);
        if (enrollment.isCourseCompleted()) {
            throw new BusinessException("all lessons already completed");
        }
        List<Lesson> lessons = enrollment.getCourse().getLessons();
        List<Lesson> completedLessons = enrollment.getCompletedLessons();

        Lesson lesson = lessons.stream().filter(l -> l.getId().equals(id)).findFirst()
                .orElseThrow(SecurityViolationException::new);

        if (completedLessons.contains(lesson)) {
            throw new BusinessException("lesson already completed");
        }

        completedLessons.add(lesson);
        if (completedLessons.size() == lessons.size()) {
            enrollment.setCourseCompleted(true);
        }
    }
}
