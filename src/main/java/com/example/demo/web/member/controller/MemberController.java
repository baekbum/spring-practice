package com.example.demo.web.member.controller;

import com.example.demo.web.member.dto.InsertMemberParam;
import com.example.demo.web.member.dto.MemberCondition;
import com.example.demo.web.member.dto.MemberDto;
import com.example.demo.web.member.dto.UpdateMemberParam;
import com.example.demo.web.member.exception.ParameterBingingException;
import com.example.demo.web.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;
    private final PasswordEncoder encoder;

    @PostMapping("/add")
    public ResponseEntity<?> addMember(@Validated @RequestBody InsertMemberParam param, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = errorCheck(bindingResult);
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }

        param.encodePassword(encoder);

        MemberDto memberDto = service.addMember(param);
        response.addCookie(new Cookie("memberId", memberDto.getId()));
        return ResponseEntity.ok(memberDto);
    }

    @GetMapping("/search/{memberNo}")
    public ResponseEntity<?> findMember(@PathVariable("memberNo") long memberNo) {
        return ResponseEntity.ok(service.findMember(memberNo));
    }

    @PostMapping("/search")
    public ResponseEntity<List<?>> findMembers(@RequestBody MemberCondition condition) {
        return ResponseEntity.ok(service.findMembers(condition));
    }

    @PostMapping("/update/{memberNo}")
    public ResponseEntity<?> updateMember(@PathVariable("memberNo") long memberNo, @RequestBody UpdateMemberParam param, HttpServletResponse response) {

        if(param.getPassword() != null) param.encodePassword(encoder);

        MemberDto memberDto = service.updateMember(memberNo, param);
        response.addCookie(new Cookie("memberId", memberDto.getId()));
        return ResponseEntity.ok(memberDto);
    }

    @PostMapping("/delete/{memberNo}")
    public ResponseEntity<?> deleteMember(@PathVariable("memberNo") long memberNo) {
        return ResponseEntity.ok(service.deleteMember(memberNo));
    }

    private static Map<String, String> errorCheck(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }

        return errors;
    }
}
