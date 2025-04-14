package com.example.demo.web.category.controller.advice;

import com.example.demo.web.category.exception.CategoryDuplicationException;
import com.example.demo.web.category.exception.NoSearchCategoryException;
import com.example.demo.web.common.dto.ErrorResult;
import com.example.demo.web.member.exception.MemberDuplicationException;
import com.example.demo.web.member.exception.NoSearchMemberException;
import com.example.demo.web.member.exception.ParameterBingingException;
import com.example.demo.web.team.exception.NoSearchTeamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.example.demo.web.category.controller")
public class CategoryControllerAdvice {

    @ExceptionHandler(CategoryDuplicationException.class)
    public ResponseEntity<ErrorResult> CategoryDuplicationException(CategoryDuplicationException e) {
        log.error("[CategoryDuplicationException] : " + e.getMessage());

        ErrorResult result = new ErrorResult("BAD", e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSearchCategoryException.class)
    public ResponseEntity<ErrorResult> NoSearchCategoryException(NoSearchCategoryException e) {
        log.error("[NoSearchCategoryException] : " + e.getMessage());

        ErrorResult result = new ErrorResult("BAD", e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(NoSearchTeamException.class)
//    public ResponseEntity<ErrorResult> NoSearchTeamException(NoSearchTeamException e) {
//        log.error("[NoSearchTeamException] : " + e.getMessage());
//
//        ErrorResult result = new ErrorResult("BAD", e.getMessage());
//        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(ParameterBingingException.class)
//    public ResponseEntity<ErrorResult> ParameterBingingException(ParameterBingingException e) {
//        log.error("[ParameterBingingException] : " + e.getMessage());
//
//        ErrorResult result = new ErrorResult("BAD", e.getMessage());
//        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
//    }

}
