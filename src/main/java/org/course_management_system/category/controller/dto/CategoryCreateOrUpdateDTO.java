package org.course_management_system.category.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateOrUpdateDTO(
        @NotBlank @Size(max = 255) String name
) {

}
