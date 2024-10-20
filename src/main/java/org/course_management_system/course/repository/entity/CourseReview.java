package org.course_management_system.course.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.course_management_system.student.repository.entity.Student;

@Entity
@Table(name = "course_review")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseReview {

    @Id
    @SequenceGenerator(name = "seq_course_review", sequenceName = "seq_course_review", allocationSize = 1, initialValue = 100)
    @GeneratedValue(generator = "seq_course_review", strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private String comment;

    private Integer rating;
}
