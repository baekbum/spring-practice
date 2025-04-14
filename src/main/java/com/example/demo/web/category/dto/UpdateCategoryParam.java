package com.example.demo.web.category.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryParam {

    @NotEmpty(message = "이름은 필수 입니다.")
    private String name;
}
