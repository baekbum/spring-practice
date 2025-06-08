package com.example.demo.web.category.controller;

import com.example.demo.web.auth.dto.userCond;
import com.example.demo.web.category.dto.CategoryCondition;
import com.example.demo.web.category.dto.InsertCategoryParam;
import com.example.demo.web.category.dto.UpdateCategoryParam;
import com.example.demo.web.category.service.CategoryService;
import com.example.demo.web.config.WebSecurityConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(WebSecurityConfig.class)
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    CategoryController controller;

    @Mock
    CategoryService service;

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
    @DisplayName("1. [추가] 카테고리 - 성공 케이스")
    void addCategorySuccess1() throws Exception {
        InsertCategoryParam outer = new InsertCategoryParam("아우터");

        String param = objectMapper.writeValueAsString(outer);

        mockMvc.perform(post("/category/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(outer.getName()));
    }

    @Test
    @DisplayName("2. [추가] 카테고리 - 실패 케이스")
    void addCategoryFail() throws Exception {
        InsertCategoryParam outer = new InsertCategoryParam("아우터");

        String param = objectMapper.writeValueAsString(outer);

        mockMvc.perform(post("/category/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(outer.getName()));

        // 똑같은 데이터를 한번 더 추가
        mockMvc.perform(post("/category/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("BAD"))
                .andExpect(jsonPath("$.message").value("해당 카테고리명은 이미 존재 합니다."));
    }

    @Test
    @DisplayName("3. [검색] 카테고리 - 성공 케이스 (ID 값으로 조회)")
    void findCategorySuccess1() throws Exception {
        InsertCategoryParam outer = new InsertCategoryParam("아우터");
        String param = objectMapper.writeValueAsString(outer);

        MvcResult mvcResult = mockMvc.perform(post("/category/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param)
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        String id = jsonNode.get("id").asText();

        mockMvc.perform(
                get("/category/search/" + id)
                        .header("J-TOKEN", JToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("아우터"));
    }

    @Test
    @DisplayName("4. [검색] 카테고리 - 실패 케이스 (ID 값으로 조회)")
    void findCategoryFail1() throws Exception {
        InsertCategoryParam outer = new InsertCategoryParam("아우터");
        String param = objectMapper.writeValueAsString(outer);

        MvcResult mvcResult = mockMvc.perform(post("/category/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param)
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        long id = jsonNode.get("id").asLong();

        mockMvc.perform(
                        get("/category/search/" + id + 1)
                                .header("J-TOKEN", JToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("5. [검색] 카테고리 - 성공 케이스 (검색 조건으로 조회)")
    void findCategorySuccess2() throws Exception {
        InsertCategoryParam outer = new InsertCategoryParam("아우터");
        InsertCategoryParam top = new InsertCategoryParam("상의");
        InsertCategoryParam bottom = new InsertCategoryParam("하의");

        mockMvc.perform(post("/category/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(outer))
                )
                .andExpect(status().isOk());

        mockMvc.perform(post("/category/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(top))
                )
                .andExpect(status().isOk());

        mockMvc.perform(post("/category/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bottom))
                )
                .andExpect(status().isOk());

        CategoryCondition condition = new CategoryCondition();
        condition.setName("의");

        MvcResult mvcResult = mockMvc.perform(post("/category/search")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(condition))
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        assertThat(jsonNode.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("6.[수정] 카테고리 - 성공 케이스")
    void updateCategorySuccess1() throws Exception {
        InsertCategoryParam outer = new InsertCategoryParam("아우터");

        MvcResult mvcResult = mockMvc.perform(post("/category/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(outer))
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        long id = jsonNode.get("id").asLong();

        UpdateCategoryParam updateCategoryParam = new UpdateCategoryParam();
        updateCategoryParam.setName("변경된 아우터");

        mockMvc.perform(
                post("/category/update/" + id)
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategoryParam))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.name").value("변경된 아우터"));
    }

    @Test
    @DisplayName("7. [삭제] 카테고리 - 성공 케이스")
    void deleteCategorySuccess1() throws Exception {
        InsertCategoryParam outer = new InsertCategoryParam("아우터");

        MvcResult mvcResult = mockMvc.perform(post("/category/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(outer))
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        long id = jsonNode.get("id").asLong();

        mockMvc.perform(
                post("/category/delete/" + id)
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/category/delete/" + id)
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }
}