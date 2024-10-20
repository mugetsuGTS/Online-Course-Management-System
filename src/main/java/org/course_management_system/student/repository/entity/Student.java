package org.course_management_system.student.repository.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.course_management_system.security.user.repository.entity.User;


@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "student")
@DiscriminatorValue("STUDENT")
public class Student extends User {

    public Student() {
        super();
    }
}
