package com.example.demo.web.item.dto;

import com.example.demo.web.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {

    private long id;
    private String name;
    private int price;
    private int quantity;
    private long categoryId;
    private String categoryName;

    public ItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
        this.categoryId = item.getCategory().getId();
        this.categoryName = item.getCategory().getName();
    }
}
