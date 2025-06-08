package com.example.demo.web.category.repository;

import com.example.demo.web.category.dto.CategoryCondition;
import com.example.demo.web.category.dto.CategoryDto;
import com.example.demo.web.category.dto.InsertCategoryParam;
import com.example.demo.web.category.dto.UpdateCategoryParam;
import com.example.demo.web.category.entity.Category;
import com.example.demo.web.category.entity.QCategory;
import com.example.demo.web.category.exception.CategoryDuplicationException;
import com.example.demo.web.category.exception.NoSearchCategoryException;
import com.example.demo.web.common.CommonUtils;
import com.example.demo.web.item.entity.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.demo.web.common.CommonUtils.*;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class SpringJpaCategoryRepository implements CategoryRepository {

    private final JPAQueryFactory queryFactory;
    private final SpringJpaCategory categoryRepository;

    @Override
    public Category addCategory(InsertCategoryParam param) {
        // 동일한 이름의 카테고리가 존재하는지 확인하는 과정
        Consumer<InsertCategoryParam> duplicateFunc = duplicateFunc();
        duplicateFunc.accept(param);

        Category newCategory = new Category(param);
        return categoryRepository.save(newCategory);
    }

    private Consumer<InsertCategoryParam> duplicateFunc() {
        return p -> {
            Optional<Category> findCategory = categoryRepository.findByName(p.getName());
            if (findCategory.isPresent()) throw new CategoryDuplicationException("해당 카테고리명은 이미 존재 합니다.");
        };
    }

    @Override
    public Category findCategory(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSearchCategoryException("해당 카테고리를 찾을 수 없습니다."));
    }

    @Override
    public List<Category> findCategories(CategoryCondition condition) {
        QCategory category = QCategory.category;

        return queryFactory
                .select(category)
                .from(category)
                .where(
                        nameLike(condition.getName(), category)
                ).fetch();
    }

    @Override
    public Category updateCategory(long id, UpdateCategoryParam param) {
        Category findCategory = findCategory(id);

        findCategory.updateCategory(param);

        return findCategory;
    }

    @Override
    public Category deleteCategory(long id) {
        Category findCategory = findCategory(id);
        categoryRepository.delete(findCategory);

        return findCategory;
    }

    private BooleanExpression nameLike(String name, QCategory category) {
        return StringUtils.hasText(name) ? category.name.like("%" + name + "%") : null;
    }
}
