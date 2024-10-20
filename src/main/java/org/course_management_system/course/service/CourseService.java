package org.course_management_system.course.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.course_management_system.category.service.CategoryService;
import org.course_management_system.course.dto.CourseCreateOrUpdateDTO;
import org.course_management_system.course.dto.CourseDTO;
import org.course_management_system.course.repository.CourseRepository;
import org.course_management_system.course.repository.entity.Course;
import org.course_management_system.course.repository.enums.Difficulty;
import org.course_management_system.exception.SecurityViolationException;
import org.course_management_system.exception.util.ExceptionUtil;
import org.course_management_system.security.user.repository.entity.User;
import org.course_management_system.security.user.service.UserService;
import org.course_management_system.util.PageView;
import org.course_management_system.util.PaginationUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository repository;
    private final CategoryService categoryService;
    private final UserService userService;

    public PageView<CourseDTO> getCoursesForInstructor(
            PaginationUtil pageRequest,
            String search,
            Long categoryId) {
        User user = userService.curentUser();
        return PageView.of(repository.getAllCoursesForInstructor(user.getId(), categoryId, search, pageRequest.build("id:desc")));
    }

    public void createCourse(CourseCreateOrUpdateDTO dto) {
        repository.save(Course.builder()
                .title(dto.title())
                .description(dto.description())
                .difficultyLevel(dto.difficultyLevel())
                .duration(dto.duration())
                .instructor(userService.curentUser())
                .categories(categoryService.getAllCategoryByIds(dto.categoryIds()))
                .build());
    }


    public void updateCourse(Long id, CourseCreateOrUpdateDTO dto) {
        Course course = lookUpCourseAndCheckInstructorAccess(id);
        course.setTitle(dto.title());
        course.setDescription(dto.description());
        course.setDifficultyLevel(dto.difficultyLevel());
        course.setDuration(dto.duration());
        course.setCategories(categoryService.getAllCategoryByIds(dto.categoryIds()));
        repository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = lookUpCourseAndCheckInstructorAccess(id);
        try {
            repository.delete(course);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            ExceptionUtil.handleDatabaseExceptions(e,
                    Map.of("update or delete on table \"course\" violates foreign key constraint", "can't delete course!"));
        }
    }

    public Course lookUpCourse(Long id) {
        return repository.findById(id)
                .orElseThrow(SecurityViolationException::new);
    }

    public Course lookUpCourseAndCheckInstructorAccess(Long id) {
        Course course = lookUpCourse(id);
        User user = userService.curentUser();
        if (!Objects.equals(user.getId(), course.getInstructor().getId())) {
            throw new SecurityViolationException();
        }
        return course;
    }

    public PageView<CourseDTO> getCoursesForStudents(String search, Difficulty difficulty, Duration durationFrom, Duration durationTo, PaginationUtil paginationUtil) {
        return PageView.of(repository.getAllCoursesForStudent(search, difficulty, durationFrom, durationTo, paginationUtil.build("id:desc")));
    }

    public void justSaveCourse(Course course) {
        repository.saveAndFlush(course);
    }

}
