package com.example.demo.web.item.entity;

import com.example.demo.web.category.entity.Category;
import com.example.demo.web.item.dto.InsertItemParam;
import com.example.demo.web.item.dto.UpdateItemParam;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_TABLE_SEQUENCE_GENERATOR")
    @SequenceGenerator(name = "ITEM_TABLE_SEQUENCE_GENERATOR", sequenceName = "ITEM_TABLE_SEQUENCE", allocationSize = 1)
    private long id;
    @NotEmpty(message = "제품명은 필수 입니다.")
    private String name;

    @NotNull(message = "가격은 필수 입니다.")
    private int price;

    @NotNull(message = "수량은 필수 입니다.")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull(message = "카테고리 값은 필수 입니다.")
    private Category category;

    public Item(InsertItemParam param, Category category) {
        this.name = param.getName();
        this.price = param.getPrice();
        this.quantity = param.getQuantity();
        this.category = category;
    }

    public void updateItem(UpdateItemParam param) {
        this.name = param.getName() != null ? param.getName() : this.name;
        this.price = param.getPrice() != null ? param.getPrice() : this.price;
        this.quantity = param.getQuantity() != null ? param.getQuantity() : this.quantity;
        //this.category = param.getCategoryId() != null ? param.getCategoryId() : this.category;
    }

    public void changeCategory(Category category) {

    }
}
