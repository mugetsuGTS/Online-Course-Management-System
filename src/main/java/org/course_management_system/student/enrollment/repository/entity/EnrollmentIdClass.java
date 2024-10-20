package org.course_management_system.student.enrollment.repository.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.course_management_system.course.repository.entity.Course;
import org.course_management_system.student.repository.entity.Student;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentIdClass implements Serializable {
    private Student student;
    private Course course;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrollmentIdClass that = (EnrollmentIdClass) o;
        return Objects.equals(student, that.student) && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }
}
