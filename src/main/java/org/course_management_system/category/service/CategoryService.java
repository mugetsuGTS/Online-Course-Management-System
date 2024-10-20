package org.course_management_system.category.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.course_management_system.category.controller.dto.CategoryCreateOrUpdateDTO;
import org.course_management_system.category.controller.dto.CategoryDTO;
import org.course_management_system.category.repository.CategoryRepository;
import org.course_management_system.category.repository.entity.Category;
import org.course_management_system.exception.SecurityViolationException;
import org.course_management_system.exception.util.ExceptionUtil;
import org.course_management_system.util.PageView;
import org.course_management_system.util.PaginationUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    public void createCategory(CategoryCreateOrUpdateDTO category) {
        try {
            repository.saveAndFlush(
                    Category.builder()
                            .name(category.name())
                            .build()
            );
        } catch (DataIntegrityViolationException e) {
            ExceptionUtil.handleDatabaseExceptions(e,
                    Map.of("uk_category_name", "category already exists!"));
        }
    }

    public void updateCategory(Long id, CategoryCreateOrUpdateDTO category) {
        Category dbCategory = repository.findById(id).orElseThrow(SecurityViolationException::new);
        try {
            dbCategory.setName(category.name());
            repository.saveAndFlush(dbCategory);
        } catch (DataIntegrityViolationException e) {
            ExceptionUtil.handleDatabaseExceptions(e,
                    Map.of("uk_category_name", "category already exists!"));
        }
    }

    public void deleteCategory(Long id) {
        Category dbCategory = repository.findById(id).orElseThrow(SecurityViolationException::new);
        try {
            repository.delete(dbCategory);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            ExceptionUtil.handleDatabaseExceptions(e,
                    Map.of("update or delete on table \"category\" violates foreign key constraint", "can't delete category!"));
        }
    }

    public PageView<CategoryDTO> getCategories(String search, PaginationUtil paginationUtil) {
        return PageView.of(repository.searchCategories(search, paginationUtil.build("id:desc")));
    }

    public List<Category> getAllCategoryByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<Category> categories = repository.findAllById(ids);
        if (categories.size() != ids.size()) {
            throw new SecurityViolationException();
        }
        return categories;
    }
}
