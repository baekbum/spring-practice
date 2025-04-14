package com.example.demo.web.category.dto;

import com.example.demo.web.category.entity.Category;
import com.example.demo.web.common.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CategoryDto extends ResponseDto {

    private long id;
    private String name;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
