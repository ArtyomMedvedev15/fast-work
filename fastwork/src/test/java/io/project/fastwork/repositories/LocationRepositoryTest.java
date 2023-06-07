package io.project.fastwork.repositories;

import io.project.fastwork.domains.Location;
import io.project.fastwork.domains.Points;
import io.project.fastwork.domains.Work;
import io.project.fastwork.services.api.MailServiceApi;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Testcontainers
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LocationRepositoryTest {
    @Autowired
    private LocationRepository locationRepository;

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

    @Test
    void SaveLocationTest_ReturnTrue(){
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_valid = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Location savedLocation = locationRepository.save(location_valid);
        Assert.assertEquals(savedLocation.getLocationPoints(), points_test_parameter);
    }

    @Test
    void FindLocationByNearbyTest_ReturnTrue() {
        List<Location>location_nearby = locationRepository.findLocationByNearby(80,60);
        assertTrue(location_nearby.size()>0);
    }

    @Test
    void FindLocationByCityTest_ReturnTrue() {
        List<Location> locationByCity = locationRepository.findLocationByCity("test");
        assertEquals("test", locationByCity.get(0).getLocationCity());
    }

    @Test
    void GetLocationByWorkIdTest_ReturnTrue() {
        Location location_get_by_id = locationRepository.getByWorkId(778L);
        assertEquals(778L, (long) location_get_by_id.getLocationWork().getId());
    }

    @Test
    void UpdateLocationTest_ReturnTrue(){
        Location location_get_by_id = locationRepository.getByWorkId(778L);
        location_get_by_id.setLocationRegion("Updated loc");
        Location updatedLocation = locationRepository.save(location_get_by_id);
        assertEquals("Updated loc", updatedLocation.getLocationRegion());
    }

    @Test
    void DeleteLocationTest_ReturnTrue(){
        Location deletedLocation = locationRepository.getByWorkId(778L);
        locationRepository.delete(deletedLocation);
        assertNull(locationRepository.getByWorkId(778L));
    }
}