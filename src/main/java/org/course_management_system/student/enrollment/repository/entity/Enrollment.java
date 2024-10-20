package org.course_management_system.student.enrollment.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.course_management_system.course.lesson.repository.entity.Lesson;
import org.course_management_system.course.repository.entity.Course;
import org.course_management_system.student.repository.entity.Student;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "enrollment")
@IdClass(EnrollmentIdClass.class)
public class Enrollment {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "course_completed")
    private boolean courseCompleted;

    @JoinTable(
            name = "enrollment_completed_lesson",
            joinColumns = {
                    @JoinColumn(name = "course_id"),
                    @JoinColumn(name = "student_id")
            },
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    @ManyToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Lesson> completedLessons = new ArrayList<>();
}
