package org.course_management_system.course.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewDTO(
        @Size(max = 255) String comment,
        @NotNull @Min(0) @Max(5) Integer rating
) {
}
