package org.course_management_system.course.lesson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LessonCreateOrUpdateDTO(
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 511) String shortDescription,
        @NotBlank @Size(max = 5000) String content
) {
}
