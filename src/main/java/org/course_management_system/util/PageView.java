package org.course_management_system.util;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageView<T>(
        List<T> data,
        Long totalElements
) {
    public static <T> PageView<T> of(Page<T> page) {
        return new PageView<>(page.toList(), page.getTotalElements());
    }
}
