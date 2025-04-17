package com.example.demo.web.item.controller;

import com.example.demo.web.auth.dto.userCond;
import com.example.demo.web.category.dto.InsertCategoryParam;
import com.example.demo.web.config.WebSecurityConfig;
import com.example.demo.web.item.dto.InsertItemParam;
import com.example.demo.web.item.dto.ItemCondition;
import com.example.demo.web.item.dto.UpdateItemParam;
import com.example.demo.web.item.service.ItemService;
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

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebSecurityConfig.class)
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ItemController controller;

    @Mock
    private ItemService service;

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
    @DisplayName("1. [추가] 아이템 - 성공 케이스")
    void addItemSuccess1() throws Exception {
        mockMvc.perform(post("/item/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼", 1500, 100, 1L)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("홈런볼"));
    }

    @Test
    @DisplayName("2. [추가] 아이템 - 성공 케이스 (이름은 같지만 카테고리가 다른 케이스")
    void addItemSuccess2() throws Exception {
        mockMvc.perform(post("/item/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼", 1500, 100, 1L)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("홈런볼"))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.categoryName").value("식료품"));

        mockMvc.perform(post("/item/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼", 1500, 100, 2L)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("홈런볼"))
                .andExpect(jsonPath("$.categoryId").value(2L))
                .andExpect(jsonPath("$.categoryName").value("의류"));
    }

    @Test
    @DisplayName("3. [추가] 아이템 - 실패 케이스 (카테고리 존재하지 않음)")
    void addItemFail1() throws Exception {
        mockMvc.perform(post("/item/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼", 1500, 100, 99999L)))
        )
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value("BAD"))
        .andExpect(jsonPath("$.message").value("해당 카테고리를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("4. [추가] 아이템 - 실패 케이스 (이름, 카테고리 중복)")
    void addItemFail2() throws Exception {
        mockMvc.perform(post("/item/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼", 1500, 100, 1L)))
        ).andExpect(status().isOk());

        mockMvc.perform(post("/item/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼", 1500, 100, 1L)))
        )
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value("BAD"))
        .andExpect(jsonPath("$.message").value("해당 아이템은 이미 존재 합니다."));
    }

    @Test
    @DisplayName("5. [검색] 아이템 - 성공 케이스 ( ID로 검색 )")
    void findItemSuccess1() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/item/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼", 1500, 100, 1L)))
        ).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        long id = jsonNode.get("id").asLong();

        mockMvc.perform(get("/item/search/" + id)
                .header("J-TOKEN", JToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("홈런볼"))
                .andExpect(jsonPath("$.price").value(1500));
    }

    @Test
    @DisplayName("6. [검색] 아이템 - 실패 케이스 ( ID로 검색 )")
    void findItemFail1() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/item/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼", 1500, 100, 1L)))
        ).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        long id = jsonNode.get("id").asLong();

        mockMvc.perform(get("/item/search/" + 0L)
                        .header("J-TOKEN", JToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("7. [검색] 아이템 - 성공 케이스 ( ID로 검색 )")
    void findItemsSuccess1() throws Exception {
        mockMvc.perform(post("/item/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼", 1500, 100, 1L)))
        ).andExpect(status().isOk());

        mockMvc.perform(post("/item/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼 우유맛", 1800, 50, 1L)))
        ).andExpect(status().isOk());

        mockMvc.perform(post("/item/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼 초코맛", 1800, 80, 1L)))
        ).andExpect(status().isOk());

        mockMvc.perform(post("/item/add")
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new InsertItemParam("가디건", 69900, 10000, 2L)))
        ).andExpect(status().isOk());

        // 검증1
        ItemCondition cond1 = new ItemCondition();
        cond1.setName("홈런볼");
        MvcResult mvcResult1 = mockMvc.perform(post("/item/search")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cond1))
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode1 = objectMapper.readTree(mvcResult1.getResponse().getContentAsString());
        assertThat(jsonNode1.size()).isEqualTo(3);

        // 검증2
        ItemCondition cond2 = new ItemCondition();
        cond2.setName("홈런볼");
        cond2.setPriceSign(">=");
        cond2.setPrice(1700);
        MvcResult mvcResult2 = mockMvc.perform(post("/item/search")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cond2))
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode2 = objectMapper.readTree(mvcResult2.getResponse().getContentAsString());
        assertThat(jsonNode2.size()).isEqualTo(2);

        // 검증3
        ItemCondition cond3 = new ItemCondition();
        cond3.setCategoryId(2L);
        MvcResult mvcResult3 = mockMvc.perform(post("/item/search")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cond3))
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode3 = objectMapper.readTree(mvcResult3.getResponse().getContentAsString());
        assertThat(jsonNode3.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("8. [수정] 아이템 - 성공 케이스")
    void updateItemSuccess1() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/item/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼", 1500, 100, 1L)))
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        long id = jsonNode.get("id").asLong();

        UpdateItemParam updateItemParam = new UpdateItemParam();
        updateItemParam.setName("홈런볼 우유맛");
        updateItemParam.setPrice(1800);

        mockMvc.perform(post("/item/update/" + id)
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateItemParam))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.name").value("홈런볼 우유맛"))
        .andExpect(jsonPath("$.price").value(1800));
    }

    @Test
    @DisplayName("9. [삭제] 아이템 - 성공 케이스")
    void deleteItemSuccess1() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/item/add")
                        .header("J-TOKEN", JToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new InsertItemParam("홈런볼", 1500, 100, 1L)))
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        long id = jsonNode.get("id").asLong();

        mockMvc.perform(post("/item/delete/" + id)
                .header("J-TOKEN", JToken)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        mockMvc.perform(get("/item/search/" + id)
                .header("J-TOKEN", JToken)
        )
        .andExpect(status().is4xxClientError());
    }
}