package org.course_management_system.security.user.repository.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    COURSE_CREATE("course:create"),
    COURSE_UPDATE("course:update"),
    COURSE_DELETE("course:delete"),
    COURSE_VIEW("course:read"),
    COURSE_ENROLL("course:enroll"),
    COURSE_REVIEW("course:review"),
    ;

    private final String permission;
}
