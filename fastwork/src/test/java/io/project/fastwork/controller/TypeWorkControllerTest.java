package io.project.fastwork.controller;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.dto.request.AuthenticationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TypeWorkControllerTest {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:9.6.18-alpine")
            .withDatabaseName("prop")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5432)
            .withInitScript("sql/initDB.sql");


    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:postgresql://localhost:%d/prop", postgreSQLContainer.getFirstMappedPort()));
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "postgres");
    }

    @Autowired
    private MockMvc mockMvc;
     private Gson gsonUtil;
    private String token;
    private final String BASE_URL = "/api/v1/typework";

    @BeforeEach
    void setUp() throws Exception {
        AuthenticationRequest jwtUser = AuthenticationRequest.builder()
                .userlogin("Login123")
                .password("Horrison@123s")
                .build();
        gsonUtil = new Gson();
        String jwtUserJson = gsonUtil.toJson(jwtUser);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signin")
                .contentType("application/json")
                .content(jwtUserJson)).andReturn();
        token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }


    @Test
    public void TypeWorkGetAll_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty());
    }

    @Test
    public void TypeWorkGetById_WithId777_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/777").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("777"));
    }

    @Test
    public void TypeWorkGetById_WithoutToken_thenStatus401() throws Exception {
        mockMvc.perform(get(BASE_URL + "/777")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void TypeWorkGetById_WithNonExistsType_thenStatus400() throws Exception {
        mockMvc.perform(get(BASE_URL + "/929").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Type work with id 929 not found"));
    }

    @Test
    public void TypeWorkSave_WithNameTest_thenStatus200() throws Exception {
        TypeWork type_work_valid = TypeWork.builder()
                .typeWorkName("Pred")
                .typeWorkDescribe("Typework typework")
                .build();

        mockMvc.perform(post(BASE_URL + "/save").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(type_work_valid))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.typeWorkName").value("Pred"));
    }

    @Test
    public void TypeWorkSave_WithInvalidData_thenStatus400() throws Exception {
        TypeWork type_work_valid = TypeWork.builder()
                .typeWorkName("12312")
                .typeWorkDescribe("Typework typework")
                .build();

        mockMvc.perform(post(BASE_URL + "/save").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(type_work_valid))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Type work data isn't correct!"));
    }

    @Test
    public void TypeWorkSave_WithExistedName_thenStatus400() throws Exception {
        TypeWork type_work_valid = TypeWork.builder()
                .typeWorkName("Type")
                .typeWorkDescribe("Typework typework")
                .build();

        mockMvc.perform(post(BASE_URL + "/save").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(type_work_valid))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Type work with name - Type already exists!"));
    }

    @Test
    public void TypeWorkUpdate_WithNameUpdated_thenStatus200() throws Exception {
        TypeWork type_work_valid = TypeWork.builder()
                .id(777L)
                .typeWorkName("Updated")
                .typeWorkDescribe("Typework typework")
                .build();

        mockMvc.perform(put(BASE_URL + "/update").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(type_work_valid))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.typeWorkName").value("Updated"));
    }

    @Test
    public void TypeWorkUpdate_WithInvalidData_thenStatus400() throws Exception {
        TypeWork type_work_valid = TypeWork.builder()
                .id(777L)
                .typeWorkName("1234")
                .typeWorkDescribe("Typework typework")
                .build();

        mockMvc.perform(put(BASE_URL + "/update").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(type_work_valid))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Type work data isn't correct!"));
    }

    @Test
    public void TypeWorkUpdate_WithExistedName_thenStatus400() throws Exception {
        TypeWork type_work_valid = TypeWork.builder()
                .id(777L)
                .typeWorkName("Test")
                .typeWorkDescribe("Typework typework")
                .build();

        mockMvc.perform(put(BASE_URL + "/update").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(type_work_valid))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Type work with name - Test already exists!"));
    }

    @Test
    public void TypeWorkDelete_WithId999_thenStatus204() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/delete/999").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.typeWorkName").value("Delete"));
    }

    @Test
    public void TypeWorkDelete_WithNonExisted_thenStatus404() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/delete/3231").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Type work with id 3231 not found"));
    }


}