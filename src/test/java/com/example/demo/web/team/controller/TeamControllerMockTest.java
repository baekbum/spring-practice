package com.example.demo.web.team.controller;

import com.example.demo.web.auth.dto.userCond;
import com.example.demo.web.config.WebSecurityConfig;
import com.example.demo.web.team.dto.InsertTeamParam;
import com.example.demo.web.team.dto.TeamCondition;
import com.example.demo.web.team.dto.TeamDto;
import com.example.demo.web.team.dto.UpdateTeamParam;
import com.example.demo.web.team.exception.NoSearchTeamException;
import com.example.demo.web.team.exception.TeamDuplicationException;
import com.example.demo.web.team.service.TeamService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
class TeamControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TeamService service;

    @InjectMocks
    private TeamController teamController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager em;

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
    @DisplayName("1. [추가] 팀 - 성공 케이스")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addTeamSuccess1() throws Exception {
        InsertTeamParam insertParam = new InsertTeamParam("개발 1팀", "TEAM", 3L);

        String param = objectMapper.writeValueAsString(insertParam);

        mockMvc.perform(post("/team/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(param)
                .with(csrf())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("개발 1팀"))
        .andExpect(jsonPath("$.rank").value("TEAM"))
        .andExpect(jsonPath("$.upperTeamId").value(3L))
        .andExpect(jsonPath("$.upperTeamName").value("개발그룹"));
    }

    @Test
    @DisplayName("2. [추가] 팀 - 성공 케이스 (다른 조건은 동일하고 상위 부서가 다른 경우)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addTeamSuccess2() throws Exception {

        String insertParam1 = objectMapper.writeValueAsString(new InsertTeamParam("개발 1팀", "TEAM", 3L));
        String insertParam2 = objectMapper.writeValueAsString(new InsertTeamParam("개발 1팀", "TEAM", 2L));

        mockMvc.perform(post("/team/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(insertParam1)
                .with(csrf())
        ).andExpect(status().isOk());

        mockMvc.perform(post("/team/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(insertParam2)
                .with(csrf())
        ).andExpect(status().isOk());
    }


    @Test
    @DisplayName("3. [추가] 팀 - 실패 케이스 (이름, 분류, 상위 팀이 같은 경우)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addTeamFail() throws Exception {
        String insertParam1 = objectMapper.writeValueAsString(new InsertTeamParam("개발 1팀", "TEAM", 2L));
        String insertParam2 = objectMapper.writeValueAsString(new InsertTeamParam("개발 1팀", "TEAM", 2L));

        mockMvc.perform(post("/team/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(insertParam1)
                .with(csrf())
        ).andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(post("/team/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(insertParam2)
                .with(csrf())
        ).andExpect(status().is4xxClientError()).andReturn();
    }

    //@Transactional(readOnly = true)
    @Test
    @DisplayName("4. [검색] 팀 - 성공 케이스 ( ID 값으로 조회 )")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void searchTeamSuccess1() throws Exception {

        String insertParam = objectMapper.writeValueAsString(new InsertTeamParam("개발 1팀", "TEAM", 3L));

        MvcResult mvcResult = mockMvc.perform(post("/team/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(insertParam)
                .with(csrf())
        ).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        long id = jsonNode.get("id").asLong();

        mockMvc.perform(get("/team/search/" + id).header("J-TOKEN", JToken))
                .andExpect(status().isOk());
    }

    //@Transactional(readOnly = true)
    @Test
    @DisplayName("5. [검색] 팀 - 실패 케이스 ( ID 값으로 조회 )")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void searchTeamFail1() throws Exception {
        mockMvc.perform(get("/team/search/999").header("J-TOKEN", JToken))
                .andExpect(status().is4xxClientError());
    }

    //@Transactional(readOnly = true)
    @Test
    @DisplayName("6. [검색] 팀 - 성공 케이스 ( 검색 조건으로 조회) ")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void searchTeamSuccess2() throws Exception {
        String insertParam1 = objectMapper.writeValueAsString(new InsertTeamParam("개발 1팀", "TEAM", 3L));
        String insertParam2 = objectMapper.writeValueAsString(new InsertTeamParam("개발 2팀", "TEAM", 3L));
        String insertParam3 = objectMapper.writeValueAsString(new InsertTeamParam("개발 3팀", "TEAM", 3L));

        mockMvc.perform(post("/team/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(insertParam1)
                .with(csrf())
        ).andExpect(status().isOk());

        mockMvc.perform(post("/team/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(insertParam2)
                .with(csrf())
        ).andExpect(status().isOk());

        mockMvc.perform(post("/team/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(insertParam3)
                .with(csrf())
        ).andExpect(status().isOk());

        // JpaRepository 를 사용할 경우에만 활성화.
        em.flush();

        TeamCondition condition = new TeamCondition("1팀", null, 3L);

        String cond = objectMapper.writeValueAsString(condition);

        MvcResult mvcResult = mockMvc.perform(get("/team/search")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cond)
        ).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        assertThat(jsonNode.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("7. [수정] 팀 - 성공 케이스")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateTeamSuccess() throws Exception {
        String insertParam = objectMapper.writeValueAsString(new InsertTeamParam("개발 1팀", "TEAM", 3L));

        MvcResult mvcResult = mockMvc.perform(post("/team/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(insertParam)
                .with(csrf())
        ).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        long id = jsonNode.get("id").asLong();

        UpdateTeamParam updateParam = new UpdateTeamParam();
        updateParam.setName("개발 2팀");
        updateParam.setRank("TEAM");
        updateParam.setUpperTeamId(2L);

        String param = objectMapper.writeValueAsString(updateParam);

        mockMvc.perform(post("/team/update/" + id)
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(param)
                .with(csrf())
        ).andExpect(status().isOk());

        mockMvc.perform(get("/team/search/" + id).header("J-TOKEN", JToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("개발 2팀"))
                .andExpect(jsonPath("$.rank").value("TEAM"))
                .andExpect(jsonPath("$.upperTeamId").value(2L));
    }

    @Test
    @DisplayName("8. [삭제] 팀 - 성공 케이스")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteTeamSuccess() throws Exception {
        String insertParam = objectMapper.writeValueAsString(new InsertTeamParam("개발 1팀", "TEAM", 3L));

        MvcResult mvcResult = mockMvc.perform(post("/team/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(insertParam)
                .with(csrf())
        ).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        long id = jsonNode.get("id").asLong();

        mockMvc.perform(post("/team/delete/" + id).header("J-TOKEN", JToken).with(csrf())).andExpect(status().isOk());

        mockMvc.perform(get("/team/search/" + id).header("J-TOKEN", JToken))
                .andExpect(status().is4xxClientError());
    }
}