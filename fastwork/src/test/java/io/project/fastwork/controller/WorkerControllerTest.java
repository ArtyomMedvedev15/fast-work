package io.project.fastwork.controller;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import io.project.fastwork.dto.request.AddWorkWorkerRequest;
import io.project.fastwork.dto.request.AuthenticationRequest;
import io.project.fastwork.dto.request.RemoveWorkWorkerRequest;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class WorkerControllerTest {
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

    private final String BASE_URL = "/api/v1/worker";

    @BeforeEach
    void setUp() throws Exception {
        AuthenticationRequest jwtUser = AuthenticationRequest.builder()
                .userlogin("Login124")
                .password("Horrison@123s")
                .build();
        gsonUtil = new Gson();
        String jwtUserJson = gsonUtil.toJson(jwtUser);
        MvcResult result = mockMvc.perform(post("/api/v1/auth/signin")
                .contentType("application/json")
                .content(jwtUserJson)).andReturn();
        token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    @Test
    public void WorkerAddWork_thenStatus200() throws Exception {
        AddWorkWorkerRequest addWorkWorkerRequest = AddWorkWorkerRequest.builder()
                .workerId(778L)
                .workId(780L)
                .build();

        mockMvc.perform(post(BASE_URL + "/addwork").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(addWorkWorkerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.workId").value(780));
    }

    @Test
    public void WorkerAddWork_WithNotExstedWork_thenStatus404() throws Exception {
        AddWorkWorkerRequest addWorkWorkerRequest = AddWorkWorkerRequest.builder()
                .workerId(778L)
                .workId(12312L)
                .build();

        mockMvc.perform(post(BASE_URL + "/addwork").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(addWorkWorkerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id - 12312 not found"));
    }

    @Test
    public void WorkerAddWork_WithNotExstedWorker_thenStatus404() throws Exception {
        AddWorkWorkerRequest addWorkWorkerRequest = AddWorkWorkerRequest.builder()
                .workerId(12312L)
                .workId(780L)
                .build();

        mockMvc.perform(post(BASE_URL + "/addwork").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(addWorkWorkerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("User with id 12312 not found"));
    }

    @Test
    public void WorkerAddWork_WithAlreadyAdded_thenStatus400() throws Exception {
        AddWorkWorkerRequest addWorkWorkerRequest = AddWorkWorkerRequest.builder()
                .workerId(781L)
                .workId(777L)
                .build();

        mockMvc.perform(post(BASE_URL + "/addwork").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(addWorkWorkerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id 777 already added to worker with id 781"));
    }

    @Test
    public void WorkerRemoveWork_thenStatus200() throws Exception {
        RemoveWorkWorkerRequest removeWorkWorkerRequest = RemoveWorkWorkerRequest.builder()
                .workerId(781L)
                .workId(777L)
                .build();

        mockMvc.perform(post(BASE_URL + "/removework").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(removeWorkWorkerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.workId").value(777));
    }

    @Test
    public void WorkerRemoveWork_WithNonExistedWorker_thenStatus404() throws Exception {
        RemoveWorkWorkerRequest removeWorkWorkerRequest = RemoveWorkWorkerRequest.builder()
                .workerId(12312L)
                .workId(777L)
                .build();

        mockMvc.perform(post(BASE_URL + "/removework").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(removeWorkWorkerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("User with id 12312 not found"));
    }


    @Test
    public void WorkerRemoveWork_WithNonExistedWork_thenStatus404() throws Exception {
        RemoveWorkWorkerRequest removeWorkWorkerRequest = RemoveWorkWorkerRequest.builder()
                .workerId(781L)
                .workId(12312L)
                .build();

        mockMvc.perform(post(BASE_URL + "/removework").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(removeWorkWorkerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id - 12312 not found"));
    }
}