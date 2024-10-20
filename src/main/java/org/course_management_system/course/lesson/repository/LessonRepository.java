package org.course_management_system.course.lesson.repository;

import org.course_management_system.course.lesson.dto.LessonDTO;
import org.course_management_system.course.lesson.repository.entity.Lesson;
import org.course_management_system.course.repository.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<LessonDTO> findByIdAndCourse(Long id, Course course);

    @Query("""
            SELECT
                l.id                AS id,
                l.title             AS title,
                l.shortDescription  AS shortDescription
            FROM Lesson l
                JOIN l.course c
            WHERE c = :course
                AND (:search IS NULL
                    OR l.title ILIKE %:search%
                    OR l.shortDescription ILIKE %:search%
                )
            """)
    Page<LessonDTO> getAllByCourseAndSearch(Course course, String search, Pageable pageable);
}
