package org.course_management_system.course.repository;

import org.course_management_system.course.repository.entity.Course;
import org.course_management_system.course.repository.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    List<CourseReview> findByCourse(Course course);
}
