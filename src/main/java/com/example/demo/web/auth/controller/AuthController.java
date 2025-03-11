package com.example.demo.web.auth.controller;

import com.example.demo.web.auth.dto.userCond;
import com.example.demo.web.auth.service.CustomUserDetailsService;
import com.example.demo.web.auth.util.JwtTokenUtil;
import com.example.demo.web.member.exception.NoSearchMemberException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder encoder;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<String> getToken(@RequestBody userCond userCond, HttpServletResponse response) {
        UserDetails details = userDetailsService.loadUserByUsername(userCond.getUsername());

        if (!encoder.matches(userCond.getPassword(), details.getPassword())) throw new NoSearchMemberException();

        String jwtToken = jwtTokenUtil.generateToken(userCond.getUsername());
        log.info("jwtToken : {}", jwtToken);

        String PREFIX = "Bearer";
        response.addCookie(new Cookie("J-TOKEN", PREFIX + " " + jwtToken));

        return ResponseEntity.ok("CREATED J-TOKEN");
    }

}
