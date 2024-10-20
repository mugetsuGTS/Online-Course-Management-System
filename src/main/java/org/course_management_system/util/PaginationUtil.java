package org.course_management_system.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PaginationUtil(
        @Min(1) Integer page,
        @Min(1) Integer size
) {

    public Integer getPage() {
        return page != null ? page : 1;
    }

    public Integer getSize() {
        return size != null ? size : Integer.MAX_VALUE;
    }

    @JsonIgnore
    public Pageable build() {
        return PageRequest.of(getPage() - 1, getSize());
    }

    @JsonIgnore
    public Pageable build(@NotNull String orderBy) {
        if (orderBy.contains(":")) {
            String[] order = orderBy.split(":");
            return PageRequest.of(getPage() - 1, getSize(), order[1].equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, order[0]);
        }
        return PageRequest.of(getPage() - 1, getSize(), Sort.Direction.ASC, orderBy);
    }
}
