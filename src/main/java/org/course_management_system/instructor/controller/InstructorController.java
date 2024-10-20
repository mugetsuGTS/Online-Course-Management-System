package org.course_management_system.instructor.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.course_management_system.security.auth.model.AuthenticationResponse;
import org.course_management_system.security.auth.model.RegisterRequest;
import org.course_management_system.security.user.repository.enums.Role;
import org.course_management_system.security.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("instructor")
public class InstructorController {

    private final UserService userService;

    @PostMapping("register")
    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse register(@RequestBody @Valid @NotNull RegisterRequest request) {
        return userService.registerUser(request, Role.INSTRUCTOR);
    }

}
