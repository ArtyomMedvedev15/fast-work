package io.project.fastwork.controller;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import io.project.fastwork.domains.Points;
import io.project.fastwork.dto.request.*;
import io.project.fastwork.services.api.LocationServiceApi;
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

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LocationControllerTest {

    @Autowired
    private LocationServiceApi locationServiceApi;

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

    private final String BASE_URL = "/api/v1/location";

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
    public void LocationGetByWorkId_WithId778_thenStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/work/778")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.locationWorkId").value(778));
    }

    @Test
    public void LocationGetByWorkId_WithNonExistedLocation_thenStatus404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/work/12312")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Location with work id 12312 not found!"));
    }

    @Test
    public void LocationGetByCity_WithCitytest_thenStatus200() throws Exception {
        LocationByCityRequest locationByCityRequest = LocationByCityRequest.builder()
                .locationCity("test")
                .build();

        mockMvc.perform(post(BASE_URL + "/findbycity")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gsonUtil.toJson(locationByCityRequest)))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].locationWorkId").value(778));
    }

    @Test
    public void LocationGetNearby_WithX80Y60_thenStatus200() throws Exception {
        LocationByNearbyRequest locationByNearbyRequest = LocationByNearbyRequest.builder()
                .locationX(80D)
                .locationY(60D)
                .build();

        mockMvc.perform(post(BASE_URL + "/findbynearby")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gsonUtil.toJson(locationByNearbyRequest)))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].locationWorkId").value(778));
    }


    @Test
    public void LocationGetNearby_WithInvalidCords_thenStatus400() throws Exception {
        LocationByNearbyRequest locationByNearbyRequest = LocationByNearbyRequest.builder()
                .locationX(-80D)
                .locationY(-60D)
                .build();

        mockMvc.perform(post(BASE_URL + "/findbynearby")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gsonUtil.toJson(locationByNearbyRequest)))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Location coordinates isn't correct!"));
    }

    @Test
    public void LocationSave_WithValidLocation_thenStatus200() throws Exception {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        LocationSaveRequest locationSaveRequest = LocationSaveRequest.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWorkId(779L)
                .locationPoints(points_test_parameter)
                .build();

        mockMvc.perform(post(BASE_URL + "/save")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gsonUtil.toJson(locationSaveRequest)))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.locationWorkId").value(779));
    }

    @Test
    public void LocationSave_WithNotExistedWork_thenStatus404() throws Exception {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        LocationSaveRequest locationSaveRequest = LocationSaveRequest.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWorkId(12312L)
                .locationPoints(points_test_parameter)
                .build();

        mockMvc.perform(post(BASE_URL + "/save")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gsonUtil.toJson(locationSaveRequest)))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Work with id - 12312 not found"));
    }

    @Test
    public void LocationSave_WithInvalidCoords_thenStatus400() throws Exception {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(-123L))
                .y(BigDecimal.valueOf(-87L))
                .build();
        LocationSaveRequest locationSaveRequest = LocationSaveRequest.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWorkId(779L)
                .locationPoints(points_test_parameter)
                .build();

        mockMvc.perform(post(BASE_URL + "/save")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gsonUtil.toJson(locationSaveRequest)))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    public void LocationUpdate_WithValidLocation_thenStatus200() throws Exception {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(77L))
                .build();
        LocationUpdateRequest locationSaveRequest = LocationUpdateRequest.builder()
                .locationId(777L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        mockMvc.perform(put(BASE_URL + "/update")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gsonUtil.toJson(locationSaveRequest)))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void LocationUpdate_WithInvalidCoords_thenStatus400() throws Exception {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(-123L))
                .y(BigDecimal.valueOf(-87L))
                .build();
        LocationUpdateRequest locationSaveRequest = LocationUpdateRequest.builder()
                .locationId(777L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        mockMvc.perform(put(BASE_URL + "/update")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gsonUtil.toJson(locationSaveRequest)))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    public void LocationDelete_WithValidLocation_thenStatus204() throws Exception {
             mockMvc.perform(delete(BASE_URL + "/delete/777")
                             .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());

    }

    @Test
    public void LocationDelete_WithNonExistedLocation_thenStatus404() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/delete/12312")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Location with id 12312 not found!"));
    }




}