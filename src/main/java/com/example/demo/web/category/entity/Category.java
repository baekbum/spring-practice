package com.example.demo.web.category.entity;

import com.example.demo.web.category.dto.InsertCategoryParam;
import com.example.demo.web.category.dto.UpdateCategoryParam;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY_TABLE_SEQUENCE_GENERATOR")
    @SequenceGenerator(name = "CATEGORY_TABLE_SEQUENCE_GENERATOR", sequenceName = "CATEGORY_TABLE_SEQUENCE", allocationSize = 1)
    private Long id;

    @NotNull
    private String name;

    public Category(InsertCategoryParam param) {
        this.name = param.getName();
    }

    public void updateCategory(UpdateCategoryParam param) {
        this.name = (param.getName() != null) ? param.getName() : this.name;
    }
}
