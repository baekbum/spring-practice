package com.example.demo.web.team.controller.advice;

import com.example.demo.web.common.dto.ErrorResult;
import com.example.demo.web.team.exception.NoSearchTeamException;
import com.example.demo.web.team.exception.TeamDuplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.example.demo.web.team.controller")
public class TeamControllerAdvice {

    @ExceptionHandler(TeamDuplicationException.class)
    public ResponseEntity<ErrorResult> teamDuplicationException(TeamDuplicationException e) {
        log.error("[TeamDuplicationException] : " + e.getMessage());

        ErrorResult result = new ErrorResult("BAD", e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSearchTeamException.class)
    public ResponseEntity<ErrorResult> NoSearchTeamException(NoSearchTeamException e) {
        log.error("[NoSearchTeamException] : " + e.getMessage());

        ErrorResult result = new ErrorResult("BAD", e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

}
