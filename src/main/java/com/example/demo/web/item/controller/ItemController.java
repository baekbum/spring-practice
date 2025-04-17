package com.example.demo.web.item.controller;

import com.example.demo.web.category.dto.UpdateCategoryParam;
import com.example.demo.web.common.CommonUtils;
import com.example.demo.web.item.dto.InsertItemParam;
import com.example.demo.web.item.dto.ItemCondition;
import com.example.demo.web.item.dto.UpdateItemParam;
import com.example.demo.web.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.web.common.CommonUtils.*;

@Slf4j
@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@Validated @RequestBody InsertItemParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return new ResponseEntity<>(getBindingResult(), HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(service.addItem(param));
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> findItem(@PathVariable("id") long id) {
        return ResponseEntity.ok(service.findItem(id));
    }

    @PostMapping("/search")
    public ResponseEntity<?> findItems(@RequestBody ItemCondition condition) {
        return ResponseEntity.ok(service.findItems(condition));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateItem(@PathVariable("id") long id, @RequestBody UpdateItemParam param) {
        return ResponseEntity.ok(service.updateItem(id, param));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable("id") long id) {
        return ResponseEntity.ok(service.deleteItem(id));
    }


}
