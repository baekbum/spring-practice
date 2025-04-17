package com.example.demo.web.item.repository;

import com.example.demo.web.category.entity.Category;
import com.example.demo.web.item.entity.Item;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpringJpaItem extends JpaRepository<Item,Long> {
    @Query("SELECT i FROM Item i JOIN FETCH i.category WHERE i.name =:name AND i.category =:category")
    Optional<Item> findByNameAndCategory(@Param("name") String name, @Param("category") Category category);
}
