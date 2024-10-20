package org.course_management_system.student.enrollment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.course_management_system.course.dto.CourseDTO;
import org.course_management_system.course.dto.ReviewDTO;
import org.course_management_system.course.dto.StudentCourseDTO;
import org.course_management_system.course.repository.CourseReviewRepository;
import org.course_management_system.course.repository.entity.Course;
import org.course_management_system.course.repository.entity.CourseReview;
import org.course_management_system.course.repository.enums.Difficulty;
import org.course_management_system.course.service.CourseService;
import org.course_management_system.exception.BusinessException;
import org.course_management_system.exception.SecurityViolationException;
import org.course_management_system.exception.util.ExceptionUtil;
import org.course_management_system.student.enrollment.repository.EnrollmentRepository;
import org.course_management_system.student.enrollment.repository.entity.Enrollment;
import org.course_management_system.student.enrollment.repository.entity.EnrollmentIdClass;
import org.course_management_system.student.repository.entity.Student;
import org.course_management_system.student.service.StudentService;
import org.course_management_system.util.PageView;
import org.course_management_system.util.PaginationUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {
    private final CourseService courseService;
    private final StudentService studentService;
    private final EnrollmentRepository repository;
    private final CourseReviewRepository reviewRepository;


    public void enrollCurrentStudentOnCourse(Long courseId) {
        Course course = courseService.lookUpCourse(courseId);
        Student student = studentService.lookupCurrentStudent();
        try {
            repository.saveAndFlush(Enrollment.builder()
                    .course(course)
                    .student(student)
                    .build());
        } catch (DataIntegrityViolationException e) {
            ExceptionUtil.handleDatabaseExceptions(e,
                    Map.of("pk_enrollment_student_course", "student already enrolled in this course!"));
        }
    }

    public void removeCurrentStudentEnrollmentOnCourse(Long courseId) {
        Course course = courseService.lookUpCourse(courseId);
        Student student = studentService.lookupCurrentStudent();
        Enrollment enrolment = repository.findById(new EnrollmentIdClass(student, course))
                .orElseThrow(SecurityViolationException::new);
        repository.delete(enrolment);
    }

    public PageView<CourseDTO> getEnrolledCourses(
            String search,
            Difficulty difficulty,
            Duration durationFrom,
            Duration durationTo,
            PaginationUtil paginationUtil) {
        Student student = studentService.lookupCurrentStudent();
        return PageView.of(repository.getAllEnrolledCourses(student, search, difficulty, durationFrom, durationTo, paginationUtil.build()));
    }

    public Enrollment lookUpEnrolment(Long courseId) {
        Course course = courseService.lookUpCourse(courseId);
        Student student = studentService.lookupCurrentStudent();
        return repository.findById(new EnrollmentIdClass(student, course))
                .orElseThrow(SecurityViolationException::new);
    }

    public StudentCourseDTO getEnrolledCourseById(Long courseId) {
        Enrollment enrollment = lookUpEnrolment(courseId);
        Course course = enrollment.getCourse();
        return StudentCourseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .duration(course.getDuration())
                .difficultyLevel(course.getDifficultyLevel())
                .categories(course.getCategories())
                .reviews(reviewRepository.findByCourse(course))
                .completed(enrollment.isCourseCompleted())
                .build();
    }

    public StudentCourseDTO getCourseById(Long courseId) {
        Course course = courseService.lookUpCourse(courseId);
        return StudentCourseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .duration(course.getDuration())
                .difficultyLevel(course.getDifficultyLevel())
                .categories(course.getCategories())
                .reviews(reviewRepository.findByCourse(course))
                .build();
    }

    public void reviewEnrolledAndCompletedCourseById(Long courseId, ReviewDTO reviewDTO) {
        Enrollment enrollment = lookUpEnrolment(courseId);
        if (!enrollment.isCourseCompleted()) {
            throw new BusinessException("non completed course cannot be reviewed!");
        }
        reviewRepository.saveAndFlush(
                CourseReview.builder()
                        .course(enrollment.getCourse())
                        .student(enrollment.getStudent())
                        .rating(reviewDTO.rating())
                        .comment(reviewDTO.comment())
                        .build()
        );
    }
}
