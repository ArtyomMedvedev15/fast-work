package io.project.fastwork.controller;

import com.google.gson.Gson;
import io.project.fastwork.domains.Role;
import io.project.fastwork.dto.request.AuthenticationRequest;
import io.project.fastwork.dto.request.RegistrationRequest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthenticationControllerTest {
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
    private final String BASE_URL = "/api/v1/auth";


    @Test
    public void SignInTest_WithValidUser_thenStatus200() throws Exception {
        AuthenticationRequest jwtUser = AuthenticationRequest.builder()
                .userlogin("Login123")
                .password("Horrison@123s")
                .build();

        gsonUtil = new Gson();

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/signin")
                        .contentType("application/json")
                        .content(gsonUtil.toJson(jwtUser)))
                .andExpect(status().isOk());
    }

    @Test
    public void SignUpTest_WithValidUser_thenStatus200() throws Exception {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .userlogin("JHaris12")
                .username("JHarissd")
                .usersoname("Bronson")
                .userpassword("QwErTy132!z")
                .useremail("monim@gmail.com")
                .userrole(Role.WORKER.name())
                .build();

        gsonUtil = new Gson();

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/signup")
                        .contentType("application/json")
                        .content(gsonUtil.toJson(registrationRequest)))
                        .andExpect(status().is4xxClientError());
    }

    @Test
    public void SignUpTest_WithAlreadyExistedUser_thenStatus200() throws Exception {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .userlogin("Logi123s")
                .username("JHarissd")
                .usersoname("Bronson")
                .userpassword("QwErTy132!z")
                .useremail("monim@gmail.com")
                .userrole(Role.WORKER.name())
                .build();

        gsonUtil = new Gson();

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/signup")
                        .contentType("application/json")
                        .content(gsonUtil.toJson(registrationRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void SignUpTest_WithInvalidUser_thenStatus200() throws Exception {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .userlogin("1234")
                .username("JHarissd")
                .usersoname("Bronson")
                .userpassword("QwErTy132!z")
                .useremail("monim@gmail.com")
                .userrole(Role.WORKER.name())
                .build();

        gsonUtil = new Gson();

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/signup")
                        .contentType("application/json")
                        .content(gsonUtil.toJson(registrationRequest)))
                .andExpect(status().isBadRequest());
    }
}