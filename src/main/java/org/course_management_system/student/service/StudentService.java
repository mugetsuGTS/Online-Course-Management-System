package org.course_management_system.student.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.course_management_system.exception.SecurityViolationException;
import org.course_management_system.security.user.service.UserService;
import org.course_management_system.student.repository.StudentRepository;
import org.course_management_system.student.repository.entity.Student;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserService userService;

    public Student lookupCurrentStudent() {
        return studentRepository.findByEmail(userService.currentUsername())
                .orElseThrow(SecurityViolationException::new);
    }
}
