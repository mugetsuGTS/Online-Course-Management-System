package org.course_management_system.category.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Category {

    @Id
    @SequenceGenerator(name = "seq_category", sequenceName = "seq_category", allocationSize = 1, initialValue = 100)
    @GeneratedValue(generator = "seq_category", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
}
