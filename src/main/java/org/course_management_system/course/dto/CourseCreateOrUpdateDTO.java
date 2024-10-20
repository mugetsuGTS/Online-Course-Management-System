package org.course_management_system.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.course_management_system.course.repository.enums.Difficulty;

import java.time.Duration;
import java.util.List;

public record CourseCreateOrUpdateDTO(
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 511) String description,
        @NotEmpty List<@NotNull Long> categoryIds,
        @NotNull Duration duration,
        @NotNull Difficulty difficultyLevel
) {
}
