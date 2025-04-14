package com.example.demo.web.category.service;

import com.example.demo.web.category.dto.CategoryCondition;
import com.example.demo.web.category.dto.CategoryDto;
import com.example.demo.web.category.dto.InsertCategoryParam;
import com.example.demo.web.category.dto.UpdateCategoryParam;
import com.example.demo.web.category.entity.Category;
import com.example.demo.web.category.repository.CategoryRepository;
import com.example.demo.web.category.repository.SpringJpaCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryDto addCategory(InsertCategoryParam param) {
        return new CategoryDto(repository.addCategory(param));
    }

    @Transactional(readOnly = true)
    public CategoryDto findCategory(long id) {
        return new CategoryDto(repository.findCategory(id));
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findCategories(CategoryCondition condition) {
        List<Category> categories = repository.findCategories(condition);

        return categories.stream()
                .map(CategoryDto::new)
                .toList();
    }

    public CategoryDto updateCategory(long id, UpdateCategoryParam param) {
        return new CategoryDto(repository.updateCategory(id, param));
    }

    public CategoryDto deleteCategory(long id) {
        return new CategoryDto(repository.deleteCategory(id));
    }
}
