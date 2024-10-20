package org.course_management_system.student.enrollment.repository;

import org.course_management_system.course.dto.CourseDTO;
import org.course_management_system.course.repository.enums.Difficulty;
import org.course_management_system.student.enrollment.repository.entity.Enrollment;
import org.course_management_system.student.enrollment.repository.entity.EnrollmentIdClass;
import org.course_management_system.student.repository.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Duration;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentIdClass> {

    @Query("""
            SELECT
                c
            FROM Enrollment e
             JOIN e.course c
            WHERE e.student = :student
                AND (:difficulty IS NULL OR c.difficultyLevel = :difficulty)
                AND (CAST(:durationFrom as biginteger) IS NULL OR c.duration >= :durationFrom)
                AND (CAST(:durationTo as biginteger) IS NULL OR c.duration <= :durationTo)
                AND (:search IS NULL
                    OR c.title ILIKE %:search%
                    OR c.description ILIKE %:search%
                    OR EXISTS(SELECT 1 FROM c.categories cc WHERE cc.name ILIKE %:search%)
                )
            """)
    Page<CourseDTO> getAllEnrolledCourses(Student student, String search, Difficulty difficulty, Duration durationFrom, Duration durationTo, Pageable pageable);
}
