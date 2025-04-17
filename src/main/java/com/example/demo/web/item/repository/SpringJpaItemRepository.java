package com.example.demo.web.item.repository;

import com.example.demo.web.category.entity.Category;
import com.example.demo.web.category.repository.SpringJpaCategoryRepository;
import com.example.demo.web.common.CommonUtils;
import com.example.demo.web.item.dto.InsertItemParam;
import com.example.demo.web.item.dto.ItemCondition;
import com.example.demo.web.item.dto.UpdateItemParam;
import com.example.demo.web.item.entity.Item;
import com.example.demo.web.item.exception.ItemDuplicationException;
import com.example.demo.web.item.exception.NoSearchItemException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

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

    private final SpringJpaItem itemRepository;
    private final SpringJpaCategoryRepository categoryRepository;
    private final EntityManager em;

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
        StringBuilder jpql = new StringBuilder("SELECT i FROM Item i JOIN FETCH i.category WHERE 1=1");
        Map<String, Object> paramMaps = new HashMap<>();

        if (condition.getName() != null) {
            jpql.append(" AND i.name LIKE :name");
            paramMaps.put("name", "%" + condition.getName() + "%");
        }

        if (condition.getPrice() != null) {
            jpql.append(" AND i.price ")
                .append(condition.getPriceSign())
                .append(":price");

            paramMaps.put("price", condition.getPrice());
        }

        if (condition.getQuantity() != null) {
            jpql.append(" AND i.quantity ")
                .append(condition.getQuantitySign())
                .append(":quantity");

            paramMaps.put("quantity", condition.getQuantity());
        }

        if (condition.getCategoryId() != null) {
            Category findCategory = categoryRepository.findCategory(condition.getCategoryId());

            jpql.append(" AND i.category =:category");
            paramMaps.put("category", findCategory);
        }

        System.out.println("쿼리 = " + jpql.toString());

        TypedQuery<Item> query = em.createQuery(jpql.toString(), Item.class);


        setQueryParam(query).accept(paramMaps);

        return query.getResultList();
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
}
