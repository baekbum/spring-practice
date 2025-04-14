package com.example.demo.web.category.repository;

import com.example.demo.web.category.dto.CategoryCondition;
import com.example.demo.web.category.dto.CategoryDto;
import com.example.demo.web.category.dto.InsertCategoryParam;
import com.example.demo.web.category.dto.UpdateCategoryParam;
import com.example.demo.web.category.entity.Category;

import java.util.List;

public interface CategoryRepository {

    Category addCategory(InsertCategoryParam param);

    Category findCategory(long id);

    List<Category> findCategories(CategoryCondition condition);

    Category updateCategory(long id, UpdateCategoryParam param);

    Category deleteCategory(long id);
}
