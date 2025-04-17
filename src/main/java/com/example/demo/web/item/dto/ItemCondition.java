package com.example.demo.web.item.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCondition {

    private String name;
    private Integer price;
    private String priceSign; // > ,>= ,= , <=, <
    private Integer quantity;
    private String quantitySign; // > ,>= ,= , <=, <
    private Long categoryId;

}
