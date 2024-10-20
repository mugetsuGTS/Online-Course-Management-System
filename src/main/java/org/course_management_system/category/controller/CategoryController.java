package org.course_management_system.category.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.course_management_system.category.controller.dto.CategoryCreateOrUpdateDTO;
import org.course_management_system.category.controller.dto.CategoryDTO;
import org.course_management_system.category.service.CategoryService;
import org.course_management_system.util.PageView;
import org.course_management_system.util.PaginationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'STUDENT')")
    public PageView<CategoryDTO> getCategories(
            @Valid @NotNull PaginationUtil paginationUtil,
            @RequestParam(name = "search", required = false) String search
    ) {
        return service.getCategories(search, paginationUtil);
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody @NotNull @Valid CategoryCreateOrUpdateDTO category) {
        service.createCategory(category);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(
            @PathVariable @NotNull Long id,
            @RequestBody @NotNull @Valid CategoryCreateOrUpdateDTO category
    ) {
        service.updateCategory(id, category);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @NotNull Long id) {
        service.deleteCategory(id);
    }
}
