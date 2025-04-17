package com.example.demo.web.member.controller;

import com.example.demo.web.auth.dto.userCond;
import com.example.demo.web.config.WebSecurityConfig;
import com.example.demo.web.member.dto.InsertMemberParam;
import com.example.demo.web.member.dto.MemberCondition;
import com.example.demo.web.member.dto.MemberDto;
import com.example.demo.web.member.dto.UpdateMemberParam;
import com.example.demo.web.member.exception.MemberDuplicationException;
import com.example.demo.web.member.exception.NoSearchMemberException;
import com.example.demo.web.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebSecurityConfig.class)
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MemberService service;

    @InjectMocks
    private MemberController memberController;

    @Autowired private ObjectMapper objectMapper;

    private String JToken;

    @BeforeEach
    void getJToken() throws Exception {
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

        JToken = cookie.getValue();
    }

    @Test
    @DisplayName("1. [추가] 멤버 - 성공 케이스")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addMemberSuccess1() throws Exception {

        InsertMemberParam shopper = new InsertMemberParam("shopper", "qwer1234", "쇼퍼", "19910828", "MANAGER", 1L);

        String param = objectMapper.writeValueAsString(shopper);

        mockMvc.perform(post("/member/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shopper.getId()))
                .andExpect(jsonPath("$.name").value(shopper.getName()))
                .andExpect(jsonPath("$.birth").value(shopper.getBrith()))
                .andExpect(jsonPath("$.rank").value(shopper.getRank()))
                .andExpect(jsonPath("$.teamId").value(1))
        ;
    }

    @Test
    @DisplayName("2. [추가] 멤버 - 성공 케이스 (다른 조건은 동일하고 아이디 값만 다른 경우)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addMemberSuccess2() throws Exception{


        String shopper = objectMapper.writeValueAsString(new InsertMemberParam("shopper", "qwer1234", "쇼퍼", "19910828", "MANAGER", 1L));
        String havana = objectMapper.writeValueAsString(new InsertMemberParam("havana", "qwer1234", "쇼퍼", "19910828", "MANAGER", 1L));

        mockMvc.perform(post("/member/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(shopper)
                .with(csrf())
        ).andExpect(status().isOk());

        mockMvc.perform(post("/member/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(havana)
                .with(csrf())
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("3. [추가] 멤버 - 실패 케이스 (ID 값 중복)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addMemberFail() throws Exception {
        String shopper1 = objectMapper.writeValueAsString(new InsertMemberParam("shopper", "qwer1234", "쇼퍼1", "19910828", "MANAGER", 1L));
        String shopper2 = objectMapper.writeValueAsString(new InsertMemberParam("shopper", "abcd1234", "쇼퍼2", "29910828", "MEMBER", 1L));

        mockMvc.perform(post("/member/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(shopper1)
                .with(csrf())
        ).andExpect(status().isOk());

        mockMvc.perform(post("/member/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shopper2)
                        .with(csrf())
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("BAD"))
                .andExpect(jsonPath("$.message").value("해당 ID는 이미 존재합니다."))
        ;
    }

    //@Transactional(readOnly = true)
    @Test
    @DisplayName("4. [검색] 멤버 - 성공 케이스 (ID 값으로 조회)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findMemberSuccess1() throws Exception {
        String shopper = objectMapper.writeValueAsString(new InsertMemberParam("shopper", "qwer1234", "쇼퍼", "19910828", "MANAGER", 1L));

        MvcResult mvcResult = mockMvc.perform(post("/member/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shopper)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        String memberNo = jsonNode.get("memberNo").asText();

        mockMvc.perform(get("/member/search/"+ memberNo).header("J-TOKEN", JToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("shopper"))
                .andExpect(jsonPath("$.teamName").value("본사"));
    }

    //@Transactional(readOnly = true)
    @Test
    @DisplayName("5. [검색] 멤버 - 실패 케이스 (ID 값으로 조회)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findMemberFail1() throws Exception {

        mockMvc.perform(get("/member/search/99").header("J-TOKEN", JToken))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("BAD"))
                .andExpect(jsonPath("$.message").value("해당 멤버를 찾을 수 없습니다."));
    }

    //@Transactional(readOnly = true)
    @Test
    @DisplayName("6. [검색] 멤버 - 성공 케이스 (검색 조건으로 조회)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findMemberSuccess2() throws Exception {
        String rank = "MEMBER";

        String shopper = objectMapper.writeValueAsString(new InsertMemberParam("shopper", "qwer1234", "쇼퍼", "19910828", rank, 1L));
        String havana = objectMapper.writeValueAsString(new InsertMemberParam("havana", "qwer1234", "하바나", "19910928", rank, 1L));
        String modern = objectMapper.writeValueAsString(new InsertMemberParam("modern", "qwer1234", "모던", "19911028", "MANAGER", 1L));

        mockMvc.perform(post("/member/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(shopper)
                .with(csrf())
        ).andExpect(status().isOk());

        mockMvc.perform(post("/member/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(havana)
                .with(csrf())
        ).andExpect(status().isOk());

        mockMvc.perform(post("/member/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(modern)
                .with(csrf())
        ).andExpect(status().isOk());


        MemberCondition condition = new MemberCondition();
        condition.setRank(rank);

        String cond = objectMapper.writeValueAsString(condition);

        MvcResult mvcResult = mockMvc.perform(post("/member/search")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cond)
                )
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(result);

        assertThat(jsonNode.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("7. [수정] 멤버 - 성공 케이스")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateMemberSuccess() throws Exception {
        // 등록
        String shopper = objectMapper.writeValueAsString(new InsertMemberParam("shopper", "qwer1234", "쇼퍼", "19910828", "MEMBER", 1L));

        mockMvc.perform(post("/member/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shopper)
                        .with(csrf())
                )
                .andExpect(status().isOk());

        // 방금 전 등록한 데이터를 조회
        MemberCondition condition = new MemberCondition();
        condition.setId("shopper");

        String cond = objectMapper.writeValueAsString(condition);

        MvcResult mvcResult = mockMvc.perform(get("/member/search")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cond)
        ).andExpect(status().isOk()).andReturn();

        // 찾은 데이터에서 ID 값을 추출
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse).get(0);

        String memberNo = jsonNode.get("memberNo").asText();

        // 해당 ID 값과 수정할 데이터를 보내고 수정된 데이터를 검증
        UpdateMemberParam updateParam = new UpdateMemberParam();
        updateParam.setName("havana");

        String havana = objectMapper.writeValueAsString(updateParam);

        mockMvc.perform(post("/member/update/"+memberNo)
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(havana)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("havana"));
    }

    @Test
    @DisplayName("8. [삭제] 멤버 - 성공 케이스")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteMemberSuccess() throws Exception {
        String rank = "MEMBER";

        String shopper = objectMapper.writeValueAsString(new InsertMemberParam("shopper", "qwer1234", "쇼퍼", "19910828", rank, 1L));
        String havana = objectMapper.writeValueAsString(new InsertMemberParam("havana", "qwer1234", "하바나", "19910928", rank, 1L));

        mockMvc.perform(post("/member/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(shopper)
                .with(csrf())
        ).andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(post("/member/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(havana)
                .with(csrf())
        ).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        String havanaNo = jsonNode.get("memberNo").asText();

        mockMvc.perform(post("/member/delete/" + havanaNo).header("J-TOKEN", JToken).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("9. 바인딩 체크")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void bindingFail() throws Exception {
        String shopper = objectMapper.writeValueAsString(new InsertMemberParam(null, null, "쇼퍼", "19910828", "MEMBER", 1L));

        MvcResult mvcResult = mockMvc.perform(post("/member/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(shopper)
                .with(csrf())
        ).andExpect(status().is4xxClientError()).andReturn();

        String jsonResponse  = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        String idErrorString = jsonNode.get("id").asText();
        String passwordErrorString = jsonNode.get("password").asText();

        assertThat(idErrorString).isEqualTo("ID는 필수 입니다.");
        assertThat(passwordErrorString).isEqualTo("비밀번호는 필수 입니다.");
    }

    @Test
    @DisplayName("10. 쿠키")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void test() throws Exception {
        String shopper = objectMapper.writeValueAsString(new InsertMemberParam("shopper", "qwer1234", "쇼퍼", "19910828", "MEMBER", 1L));

        MvcResult mvcResult = mockMvc.perform(post("/member/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(shopper)
                .with(csrf())
        ).andExpect(status().isOk()).andReturn();

        Cookie memberId = mvcResult.getResponse().getCookie("memberId");

        assertThat(memberId).isNotNull();
        assertThat(memberId.getValue()).isEqualTo("shopper");
    }
}