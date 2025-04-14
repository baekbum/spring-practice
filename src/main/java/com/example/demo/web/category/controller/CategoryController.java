package com.example.demo.web.category.controller;

import com.example.demo.web.category.dto.CategoryCondition;
import com.example.demo.web.category.dto.CategoryDto;
import com.example.demo.web.category.dto.InsertCategoryParam;
import com.example.demo.web.category.dto.UpdateCategoryParam;
import com.example.demo.web.category.service.CategoryService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.web.common.CommonUtils.getBindingResult;

@Slf4j
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@Validated @RequestBody InsertCategoryParam param, BindingResult bindingResult, HttpServletResponse response){
        if (bindingResult.hasErrors()) return new ResponseEntity<>(getBindingResult().apply(bindingResult), HttpStatus.BAD_REQUEST);

        CategoryDto categoryDto = service.addCategory(param);

        return ResponseEntity.ok(categoryDto);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> findCategory(@PathVariable("id") long id) {
        return ResponseEntity.ok(service.findCategory(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> findCategories(@RequestBody CategoryCondition condition) {
        return ResponseEntity.ok(service.findCategories(condition));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") long id, @RequestBody UpdateCategoryParam param) {
        return ResponseEntity.ok(service.updateCategory(id, param));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") long id) {
        return ResponseEntity.ok(service.deleteCategory(id));
    }
}
