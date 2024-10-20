package org.course_management_system.course.lesson.dto;

import lombok.Builder;

@Builder
public record StudentLessonDTO(
        Long id,
        String title,
        String shortDescription,
        boolean completed
) {
}
