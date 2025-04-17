package com.example.demo.web.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemParam {

    private long id;
    private String name;
    private Integer price;
    private Integer quantity;
    private Long categoryId;
}
