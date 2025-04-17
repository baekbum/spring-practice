package com.example.demo.web.item.controller.advice;

import com.example.demo.web.category.exception.CategoryDuplicationException;
import com.example.demo.web.category.exception.NoSearchCategoryException;
import com.example.demo.web.common.dto.ErrorResult;
import com.example.demo.web.item.exception.ItemDuplicationException;
import com.example.demo.web.item.exception.NoSearchItemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.example.demo.web.item.controller")
public class ItemControllerAdvice {

    @ExceptionHandler(ItemDuplicationException.class)
    public ResponseEntity<ErrorResult> ItemDuplicationException(ItemDuplicationException e) {
        log.error("[ItemDuplicationException] : " + e.getMessage());

        ErrorResult result = new ErrorResult("BAD", e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSearchItemException.class)
    public ResponseEntity<ErrorResult> NoSearchItemException(NoSearchItemException e) {
        log.error("[NoSearchItemException] : " + e.getMessage());

        ErrorResult result = new ErrorResult("BAD", e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSearchCategoryException.class)
    public ResponseEntity<ErrorResult> NoSearchCategoryException(NoSearchCategoryException e) {
        log.error("[NoSearchCategoryException] : " + e.getMessage());

        ErrorResult result = new ErrorResult("BAD", e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
