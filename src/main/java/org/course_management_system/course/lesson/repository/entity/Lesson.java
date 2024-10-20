package org.course_management_system.course.lesson.repository.entity;

import jakarta.persistence.Column;
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
import org.course_management_system.course.repository.entity.Course;

@Entity
@Table(name = "lesson")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Lesson {

    @Id
    @SequenceGenerator(name = "seq_lesson", sequenceName = "seq_lesson", allocationSize = 1, initialValue = 100)
    @GeneratedValue(generator = "seq_lesson", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
}

