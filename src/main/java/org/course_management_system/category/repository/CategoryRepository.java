package org.course_management_system.category.repository;

import org.course_management_system.category.controller.dto.CategoryDTO;
import org.course_management_system.category.repository.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
            SELECT
               c.id        AS id,
               c.name      AS name
            FROM Category c
            WHERE (:search IS NULL OR c.name ILIKE %:search%)
            """)
    Page<CategoryDTO> searchCategories(String search, Pageable pageable);
}
