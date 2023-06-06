package io.project.fastwork.controller;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.dto.request.*;
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
class WorkControllerTest {
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

    private final String BASE_URL = "/api/v1/work";

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
    public void WorkGetAll_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].workId").isNotEmpty());
    }


    @Test
    public void WorkGetAllOpenedWork_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/openedwork")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].statusWork").value("OPEN"));
    }

    @Test
    public void WorkGetAllClosedWork_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/closedwork").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].statusWork").value("CLOSE"));
    }

    @Test
    public void WorkGetAllExceptionWork_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/exceptionwork").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].statusWork").value("EXPECTATION"));
    }

    @Test
    public void WorkGetById_WithId777_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/777").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.workId").value(777));
    }

    @Test
    public void WorkGetById_WithUnexistedId_thenStatus404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/12312").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id - 12312 not found"));
    }

    @Test
    public void WorkFindByName_WithNameUniq_thenStatus200() throws Exception {
        WorkByNameRequest workByNameRequest = WorkByNameRequest.builder()
                .workname("Uniq")
                .build();

        mockMvc.perform(post(BASE_URL + "/findbyname")
                        .content(gsonUtil.toJson(workByNameRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].workId").isNotEmpty());
    }

    @Test
    public void WorkFindByType_WithTypeId777_thenStatus200() throws Exception {
        WorkByTypeRequest workByTypeRequest = WorkByTypeRequest.builder()
                .id_type(777L)
                .build();

        mockMvc.perform(post(BASE_URL + "/findbytype")
                        .content(gsonUtil.toJson(workByTypeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].workTypeId").value(777));
    }

    @Test
    public void WorkFindByType_WithUnexistedType_thenStatus404() throws Exception {
        WorkByTypeRequest workByTypeRequest = WorkByTypeRequest.builder()
                .id_type(5124L)
                .build();

        mockMvc.perform(post(BASE_URL + "/findbytype")
                        .content(gsonUtil.toJson(workByTypeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                 .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Type work with id 5124 not found"));
    }

    @Test
    public void WorkSave_WithValidWork_thenStatus200() throws Exception {
        WorkSaveRequest workSave = WorkSaveRequest.builder()
                .workName("Saved")
                .workDescribe("Saved workworkwork")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workTypeId(777L)
                .workHirerId(881L)
                .build();

        mockMvc.perform(post(BASE_URL + "/save").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(workSave))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.workName").value("Saved"));
    }

    @Test
    public void WorkSave_WithInValidWork_thenStatus400() throws Exception {
        WorkSaveRequest workSave = WorkSaveRequest.builder()
                .workName("1234")
                .workDescribe("Saved workworkwork")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workTypeId(777L)
                .workHirerId(881L)
                .build();

        mockMvc.perform(post(BASE_URL + "/save")
                        .content(gsonUtil.toJson(workSave)).header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work data isn't correct, try yet!"));
    }

    @Test
    public void WorkSave_WithExistedName_thenStatus400() throws Exception {
        WorkSaveRequest workSave = WorkSaveRequest.builder()
                .workName("Unique")
                .workDescribe("Saved workworkwork")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workTypeId(777L)
                .workHirerId(881L)
                .build();

        mockMvc.perform(post(BASE_URL + "/save").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(workSave))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with name Unique already exists!"));
    }

    @Test
    public void WorkUpdate_WithValidWork_thenStatus200() throws Exception {
        WorkUpdateRequest workUpdate = WorkUpdateRequest.builder()
                .workId(777L)
                .workName("Unique")
                .workDescribe("Updated workworkwork")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workTypeId(777L)
                .workHirerId(881L)
                .build();

        mockMvc.perform(put(BASE_URL + "/update").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(workUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.workName").value("Unique"));
    }

    @Test
    public void WorkUpdate_WithInvalidWork_thenStatus400() throws Exception {
        WorkUpdateRequest workUpdate = WorkUpdateRequest.builder()
                .workId(777L)
                .workName("123412")
                .workDescribe("Updated workworkwork")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workTypeId(777L)
                .workHirerId(881L)
                .build();

        mockMvc.perform(put(BASE_URL + "/update").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(workUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work data isn't correct, try yet!"));
    }

    @Test
    public void WorkUpdate_WithNameExistedWork_thenStatus400() throws Exception {
        WorkUpdateRequest workUpdate = WorkUpdateRequest.builder()
                .workId(777L)
                .workName("Teste")
                .workDescribe("Updated workworkwork")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workTypeId(777L)
                .workHirerId(881L)
                .build();

        mockMvc.perform(put(BASE_URL + "/update").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(workUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with name Teste already exists!"));
    }

    @Test
    public void WorkUpdate_WithNonExistedWorkId_thenStatus404() throws Exception {
        WorkUpdateRequest workUpdate = WorkUpdateRequest.builder()
                .workId(6753L)
                .workName("Teste")
                .workDescribe("Updated workworkwork")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workTypeId(777L)
                .workHirerId(881L)
                .build();

        mockMvc.perform(put(BASE_URL + "/update").header("Authorization", "Bearer " + token)
                        .content(gsonUtil.toJson(workUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id - 6753 not found"));
    }

    @Test
    public void WorkClose_WithId777_thenStatus200() throws Exception {
        mockMvc.perform(post(BASE_URL + "/closework/777").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusWork").value("CLOSE"));
    }

    @Test
    public void WorkClose_WithUnexistedId_thenStatus404() throws Exception {
        mockMvc.perform(post(BASE_URL + "/closework/12312").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id - 12312 not found"));
    }

    @Test
    public void WorkException_WithId777_thenStatus200() throws Exception {
        mockMvc.perform(post(BASE_URL + "/exceptionwork/777").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusWork").value("EXPECTATION"));
    }

    @Test
    public void WorkException_WithUnexistedId_thenStatus404() throws Exception {
        mockMvc.perform(post(BASE_URL + "/exceptionwork/12312").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id - 12312 not found"));
    }

    @Test
    public void WorkOpen_WithId777_thenStatus200() throws Exception {
        mockMvc.perform(post(BASE_URL + "/openwork/777").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusWork").value("OPEN"));
    }

    @Test
    public void WorkOpen_WithUnexistedId_thenStatus404() throws Exception {
        mockMvc.perform(post(BASE_URL + "/openwork/12312").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id - 12312 not found"));
    }

}