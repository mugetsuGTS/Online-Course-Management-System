package org.course_management_system.course.dto;

import org.course_management_system.category.controller.dto.CategoryDTO;
import org.course_management_system.course.repository.enums.Difficulty;

import java.time.Duration;
import java.util.List;

public interface CourseDTO {

    Long getId();

    String getTitle();

    String getDescription();

    List<CategoryDTO> getCategories();

    Duration getDuration();

    Difficulty getDifficultyLevel();
}
