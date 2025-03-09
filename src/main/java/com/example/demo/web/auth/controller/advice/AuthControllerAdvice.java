package com.example.demo.web.auth.controller.advice;

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
@RestControllerAdvice("com.example.demo.web.auth.controller")
public class AuthControllerAdvice {

    @ExceptionHandler(NoSearchMemberException.class)
    public ResponseEntity<ErrorResult> MemberDuplicationException(NoSearchMemberException e) {
        log.error("[NoSearchMemberException] : " + e.getMessage());

        ErrorResult result = new ErrorResult("BAD", "올바르지 않은 사용자 정보입니다.");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
