package org.course_management_system.security.user.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.course_management_system.exception.SecurityViolationException;
import org.course_management_system.instructor.repository.Instructor;
import org.course_management_system.security.auth.model.AuthenticationResponse;
import org.course_management_system.security.auth.model.RegisterRequest;
import org.course_management_system.security.auth.service.AuthenticationService;
import org.course_management_system.security.user.repository.UserRepository;
import org.course_management_system.security.user.repository.entity.User;
import org.course_management_system.security.user.repository.enums.Role;
import org.course_management_system.student.repository.entity.Student;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository<User> userRepository;
    private final AuthenticationService authenticationService;

    public User lookUpUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(SecurityViolationException::new);
    }

    public User curentUser() {
        String username = currentUsername();
        return lookUpUserByEmail(username);
    }

    public String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((User) authentication.getPrincipal()).getUsername();
    }

    public AuthenticationResponse registerUser(RegisterRequest request, Role role) {
        User.UserBuilder<?, ?> builder = switch (role) {
            case STUDENT -> Student.builder();
            case INSTRUCTOR -> Instructor.builder();
        };

        builder.email(request.getEmail())
                .role(role)
                .firstname(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        return authenticationService.register(builder.build(), request.getPassword());
    }
}
