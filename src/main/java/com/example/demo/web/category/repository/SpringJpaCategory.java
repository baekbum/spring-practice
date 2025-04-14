package com.example.demo.web.category.repository;

import com.example.demo.web.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringJpaCategory extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
