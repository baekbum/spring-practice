package com.example.demo.web.auth.service;

import com.example.demo.web.auth.controller.AuthController;
import com.example.demo.web.auth.dto.userCond;
import com.example.demo.web.auth.util.JwtTokenUtil;
import com.example.demo.web.config.WebSecurityConfig;
import com.example.demo.web.member.controller.MemberController;
import com.example.demo.web.member.dto.InsertMemberParam;
import com.example.demo.web.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebSecurityConfig.class)
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class JwtTokenTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenUtil util;

    @Mock
    private AuthController authController;

    @Mock
    private MemberService service;

    @InjectMocks
    private MemberController memberController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("토큰 발급")
    void getToken() throws Exception {

        String username = "admin";
        String password = "qwer1234";

        userCond userCond = new userCond(username, password);

        MvcResult mvcResult = mockMvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCond))
        )
        .andExpect(status().isOk())
        .andReturn();

        Cookie cookie = mvcResult.getResponse().getCookie("J-TOKEN");
        System.out.println("cookie = " + cookie.getValue());

        assertThat(cookie).isNotNull();
    }

    @Test
    @DisplayName("토큰 발급 실패 (사용자 정보가 올바르지 않음)")
    void getTokenFail() throws Exception {

        String username = "admin";
        String password = "1234qwer";

        userCond userCond = new userCond(username, password);

        MvcResult mvcResult = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCond))
                )
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    @DisplayName("토큰이 없을 때 접근 불가")
    void requestDenied() throws Exception {
        mockMvc.perform(get("/member/search/" + 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("토큰을 헤더에 넣은 상태로 요청")
    void requestAccess() throws Exception {
        // 토큰 발급 받기
        String username = "admin";
        String password = "qwer1234";

        userCond userCond = new userCond(username, password);

        MvcResult mvcResult = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCond))
                )
                .andExpect(status().isOk())
                .andReturn();

        Cookie cookie = mvcResult.getResponse().getCookie("J-TOKEN");

        String JToken = cookie.getValue();

        InsertMemberParam shopper = new InsertMemberParam("shopper", "qwer1234", "쇼퍼", "19910828", "MANAGER", 3L);

        mockMvc.perform(post("/member/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopper))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shopper.getId()))
                .andExpect(jsonPath("$.name").value(shopper.getName()))
                .andExpect(jsonPath("$.birth").value(shopper.getBrith()))
                .andExpect(jsonPath("$.rank").value(shopper.getRank()))
                .andExpect(jsonPath("$.teamId").value(shopper.getTeamId()));
    }

}
