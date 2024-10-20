package org.course_management_system.security.user.repository.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {

    STUDENT(Set.of(
            Permission.COURSE_VIEW,
            Permission.COURSE_REVIEW,
            Permission.COURSE_ENROLL
    )),
    INSTRUCTOR(Set.of(
            Permission.COURSE_CREATE,
            Permission.COURSE_UPDATE,
            Permission.COURSE_DELETE,
            Permission.COURSE_VIEW
    ));

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
