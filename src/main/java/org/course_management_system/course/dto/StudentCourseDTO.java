package org.course_management_system.course.dto;

import lombok.Builder;
import org.course_management_system.category.repository.entity.Category;
import org.course_management_system.course.repository.entity.CourseReview;
import org.course_management_system.course.repository.enums.Difficulty;

import java.time.Duration;
import java.util.List;

@Builder
public record StudentCourseDTO(
        Long id,
        String title,
        String description,
        List<Category> categories,
        Duration duration,
        Difficulty difficultyLevel,
        Boolean completed,
        List<CourseReview> reviews
) {
}
