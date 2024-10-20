package org.course_management_system.course.repository;

import org.course_management_system.course.dto.CourseDTO;
import org.course_management_system.course.repository.entity.Course;
import org.course_management_system.course.repository.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Duration;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
            SELECT
                c
            FROM Course c
                JOIN c.instructor instructor
            WHERE instructor.id = :instructorId
                AND (:search IS NULL
                    OR c.title ILIKE %:search%
                    OR c.description ILIKE %:search%)
                AND (:categoryId IS NULL OR EXISTS(SELECT 1 FROM c.categories cc WHERE cc.id = :categoryId))
            """)
    Page<CourseDTO> getAllCoursesForInstructor(Long instructorId, Long categoryId, String search, Pageable pageable);

    @Query("""
            SELECT
                c
            FROM Course c
            WHERE (:difficulty IS NULL OR c.difficultyLevel = :difficulty)
                AND (:durationFrom IS NULL OR c.duration >= :durationFrom)
                AND (:durationTo IS NULL OR c.duration <= :durationTo)
                AND (:search IS NULL
                    OR c.title ILIKE %:search%
                    OR c.description ILIKE %:search%
                    OR EXISTS(SELECT 1 FROM c.categories cc WHERE cc.name ILIKE %:search%)
                )
            """)
    Page<CourseDTO> getAllCoursesForStudent(String search, Difficulty difficulty, Duration durationFrom, Duration durationTo, Pageable pageable);
}
