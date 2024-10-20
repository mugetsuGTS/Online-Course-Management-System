package org.course_management_system.student.repository;

import org.course_management_system.security.user.repository.UserRepository;
import org.course_management_system.student.repository.entity.Student;

public interface StudentRepository extends UserRepository<Student> {
}
