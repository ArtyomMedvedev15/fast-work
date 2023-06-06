package io.project.fastwork.controller;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.domains.WorkApplication;
import io.project.fastwork.dto.request.AuthenticationRequest;
import io.project.fastwork.dto.request.WorkApplicationSaveRequest;
import io.project.fastwork.dto.request.WorkSaveRequest;
import io.project.fastwork.services.api.MailServiceApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class WorkApplicationControllerTest {
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

    @MockBean
    private MailServiceApi mailService;

    private final String BASE_URL = "/api/v1/workapplication";

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
    public void WorkApplicationGetAll_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/all").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty());
    }

    @Test
    public void WorkApplicationGetByWork_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/findbywork/779").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(777));
    }

    @Test
    public void WorkApplicationGetByWork_WithNonExistedWork_thenStatus404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/findbywork/12312").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id - 12312 not found"));
    }

    @Test
    public void WorkApplicationGetByWorker_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/findbyworker/881").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(777));
    }

    @Test
    public void WorkApplicationGetByWorker_WithNonExistedWorker_thenStatus404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/findbyworker/12312").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("User with id 12312 not found"));
    }

    @Test
    public void WorkApplicationSave_WithValidWorkApplication_thenStatus200() throws Exception {
        WorkApplicationSaveRequest workApplicationSaveRequest = WorkApplicationSaveRequest.builder()
                .workerId(777L)
                .workId(777L)
                .build();

        mockMvc.perform(post(BASE_URL + "/save").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(workApplicationSaveRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.work.workId").value(777L));
    }

    @Test
    public void WorkApplicationSave_WithNonExistedUser_thenStatus404() throws Exception {
        WorkApplicationSaveRequest workApplicationSaveRequest = WorkApplicationSaveRequest.builder()
                .workerId(12312L)
                .workId(777L)
                .build();

        mockMvc.perform(post(BASE_URL + "/save").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(workApplicationSaveRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("User with id 12312 not found"));
    }

    @Test
    public void WorkApplicationSave_WithNonExistedWork_thenStatus404() throws Exception {
        WorkApplicationSaveRequest workApplicationSaveRequest = WorkApplicationSaveRequest.builder()
                .workerId(778L)
                .workId(12312L)
                .build();

        mockMvc.perform(post(BASE_URL + "/save").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(workApplicationSaveRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id - 12312 not found"));
    }

    @Test
    public void WorkApplicationSave_WithAlreadySendWorkApplication_thenStatus400() throws Exception {
        WorkApplicationSaveRequest workApplicationSaveRequest = WorkApplicationSaveRequest.builder()
                .workerId(881L)
                .workId(779L)
                .build();

        mockMvc.perform(post(BASE_URL + "/save").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(workApplicationSaveRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work application already send!"));
    }


    @Test
    public void WorkApplicationApprove_WithId777_thenStatus200() throws Exception {
        mockMvc.perform(put(BASE_URL + "/approve/777").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(777));
    }

    @Test
    public void WorkApplicationApprove_WithNonExistedWorkApplication_thenStatus404() throws Exception {
        mockMvc.perform(put(BASE_URL + "/approve/12312").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                 .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work application with id 12312 not found!"));
    }

    @Test
    public void WorkApplicationApprove_WithAlreadyAdded_thenStatus400() throws Exception {
        mockMvc.perform(put(BASE_URL + "/approve/888").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id 777 already added to worker with id 781"));
    }

    @Test
    public void WorkApplicationReject_WithId777_thenStatus200() throws Exception {
        mockMvc.perform(put(BASE_URL + "/reject/777").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(777));
    }

    @Test
    public void WorkApplicationReject_WithNonExistedWorkApplication_thenStatus404() throws Exception {
        mockMvc.perform(put(BASE_URL + "/reject/12312").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work application with id 12312 not found!"));
    }
}