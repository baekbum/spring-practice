package com.example.demo.web.item.repository;

import com.example.demo.web.category.entity.Category;
import com.example.demo.web.category.entity.QCategory;
import com.example.demo.web.category.repository.SpringJpaCategoryRepository;
import com.example.demo.web.common.CommonUtils;
import com.example.demo.web.item.dto.InsertItemParam;
import com.example.demo.web.item.dto.ItemCondition;
import com.example.demo.web.item.dto.UpdateItemParam;
import com.example.demo.web.item.entity.Item;
import com.example.demo.web.item.entity.QItem;
import com.example.demo.web.item.exception.ItemDuplicationException;
import com.example.demo.web.item.exception.NoSearchItemException;
import com.example.demo.web.member.entity.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static com.example.demo.web.common.CommonUtils.*;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class SpringJpaItemRepository implements ItemRepository {

    private final JPAQueryFactory queryFactory;
    private final SpringJpaItem itemRepository;
    private final SpringJpaCategoryRepository categoryRepository;

    @Override
    public Item addItem(InsertItemParam param) {
        Category findCategory = categoryRepository.findCategory(param.getCategoryId());

        // 이미 같은 아이템이 존재하는지 확인하는 작업
        duplicateFunc().accept(param, findCategory);

        return itemRepository.save(new Item(param, findCategory));
    }

    private BiConsumer<InsertItemParam, Category> duplicateFunc() {
        return (i, c) -> {

            Optional<Item> findItem = itemRepository.findByNameAndCategory(i.getName(), c);
            if (findItem.isPresent()) throw new ItemDuplicationException("해당 아이템은 이미 존재 합니다.");
        };
    }

    @Override
    public Item findItem(long id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new NoSearchItemException("해당 아이템을 찾을 수 없습니다.")
        );
    }

    @Override
    public List<Item> findItems(ItemCondition condition) {
        QItem item = QItem.item;
        QCategory category = QCategory.category;

        return queryFactory
                .select(item)
                .from(item)
                .leftJoin(item.category, category)
                .where(
                        nameLike(condition.getName(), item),
                        priceCompare(condition.getPrice(), condition.getPriceSign(), item),
                        quantityCompare(condition.getQuantity(), condition.getPriceSign(), item),
                        categoryIdEq(condition.getCategoryId(), item)
                ).fetch();
    }

    @Override
    public Item updateItem(long id, UpdateItemParam param) {
        Item findItem = findItem(id);
        findItem.updateItem(param);

        if (param.getCategoryId() != null) {
            findItem.changeCategory(categoryRepository.findCategory(param.getCategoryId()));
        }

        return findItem;
    }

    @Override
    public Item deleteItem(long id) {
        Item findItem = findItem(id);
        itemRepository.delete(findItem);

        return findItem;
    }

    private BooleanExpression nameLike(String name, QItem item) {
        return StringUtils.hasText(name) ? item.name.like("%" + name + "%") : null;
    }

    private BooleanExpression priceCompare(Integer price, String sign, QItem item) {
        if (price == null) return null;
        // > ,>= ,= , <=, <
        switch (sign) {
            case ">" :
                return item.price.gt(price);
            case ">=" :
                return item.price.goe(price);
            case "=" :
                return item.price.eq(price);
            case "<=" :
                return item.price.loe(price);
            case  "<" :
                return item.price.lt(price);
            default:
                return null;
        }
    }

    private BooleanExpression quantityCompare(Integer quantity, String sign, QItem item) {
        if (quantity == null) return null;
        // > ,>= ,= , <=, <
        switch (sign) {
            case ">" :
                return item.quantity.gt(quantity);
            case ">=" :
                return item.quantity.goe(quantity);
            case "=" :
                return item.quantity.eq(quantity);
            case "<=" :
                return item.quantity.loe(quantity);
            case  "<" :
                return item.quantity.lt(quantity);
            default:
                return null;
        }
    }

    private BooleanExpression categoryIdEq(Long id, QItem item) {
        if (id == null) return null;
        Category category = categoryRepository.findCategory(id);

        return item.category.eq(category);
    }
}
