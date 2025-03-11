package com.example.demo.web.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class userCond {

    private String username;
    private String password;
}
