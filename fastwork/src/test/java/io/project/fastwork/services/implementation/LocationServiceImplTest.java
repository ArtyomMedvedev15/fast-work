package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.Location;
import io.project.fastwork.domains.Points;
import io.project.fastwork.domains.Work;
import io.project.fastwork.repositories.LocationRepository;
import io.project.fastwork.services.api.LocationServiceApi;
import io.project.fastwork.services.exception.LocationNotFound;
import io.project.fastwork.services.exception.LocationWithInvalidArguments;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LocationServiceImplTest {

    @Autowired
    private LocationServiceApi locationService;

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
    void SaveLocation_WithWorkId777_ReturnTrue() throws LocationWithInvalidArguments, LocationNotFound {
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
        locationService.saveLocation(location_valid);
        assertNotNull(locationService.getByWork(777L));
    }

    @Test
    void SaveLocation_WithWithExistedLocation_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(778L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Invalid data for location, or try to save second location for work, throw exception"));
    }

    @Test
    void SaveLocation_WithIncorrectCityLengthLess4_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Tes")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void SaveLocation_WithIncorrectCityMoreLengthThan30_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("TestTestTestTestTestTestTestTest")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void SaveLocation_WithIncorrectCountryLengthLess4_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Test")
                .locationCountry("Tes")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void SaveLocation_WithIncorrectCountryLengthMoreThan30_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Test")
                .locationCountry("TestTestTestTestTestTestTestTest")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void SaveLocation_WithIncorrectRegionLengthLess4_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Tes")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void SaveLocation_WithIncorrectRegionLengthMoreThan45_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("TestTestTestTestTestTestTestTestTestTestTestTest")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void SaveLocation_WithIncorrectStreetLengthLess4_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Tes")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void SaveLocation_WithIncorrectStreetLengthMoreThan45_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("TestTestTestTestTestTestTestTestTestTestTestTest")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void SaveLocation_WithIncorrectMapCordXLess0_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(-123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void SaveLocation_WithIncorrectMapCordXMoreThan180_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(1223L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void SaveLocation_WithIncorrectMapCordYLess0_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(-87L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void SaveLocation_WithIncorrectMapCordYMoreThan90_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(91L))
                .build();
        Location location_invalid = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.saveLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithCorrectCity_ReturnTrue() throws LocationNotFound, LocationWithInvalidArguments {
        Location location_valid = locationService.getByWork(778L);
        location_valid.setLocationCity("Minsk");
        locationService.updateLocation(location_valid);
        assertEquals("Minsk", locationService.getByWork(778L).getLocationCity());
    }
    @Test
    void UpdateLocation_WithInCorrectCityLengthLess4_ThrowException() throws LocationNotFound {
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationCity("Min");
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
     }

    @Test
    void UpdateLocation_WithInCorrectCityLengthMore30_ThrowException() throws LocationNotFound {
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationCity("MinskMinskMinskMinskMinskMinskMinsk");
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithInCorrectCountryLengthLess4_ThrowException() throws LocationNotFound {
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationCountry("Bel");
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void UpdateLocation_WithInCorrectCountryLengthMore30_ThrowException() throws LocationNotFound {
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationCountry("MinskMinskMinskMinskMinskMinskMinsk");
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void UpdateLocation_WithInCorrectRegionLengthLess4_ThrowException() throws LocationNotFound {
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationRegion("Bel");
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void UpdateLocation_WithInCorrectRegionLengthMore40_ThrowException() throws LocationNotFound {
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationCountry("MinskMinskMinskMinskMinskMinskMinskMinskMinsk");
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void UpdateLocation_WithInCorrectStreetLengthLess4_ThrowException() throws LocationNotFound {
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationStreet("Bel");
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void UpdateLocation_WithInCorrectStreetLengthMore40_ThrowException() throws LocationNotFound {
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationStreet("MinskMinskMinskMinskMinskMinskMinskMinskMinskMinsk");
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void UpdateLocation_WithIncorrectMapCordXLess0_ThrowException() throws LocationNotFound {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(-123L))
                .y(BigDecimal.valueOf(91L))
                .build();

        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationPoints(points_test_parameter);
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void UpdateLocation_WithIncorrectMapCordXMoreThan180_ThrowException() throws LocationNotFound {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(1223L))
                .y(BigDecimal.valueOf(91L))
                .build();

        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationPoints(points_test_parameter);
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void UpdateLocation_WithIncorrectMapCordYLess0_ThrowException() throws LocationNotFound {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(-91L))
                .build();

        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationPoints(points_test_parameter);
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void UpdateLocation_WithIncorrectMapCordYMoreThan90_ThrowException() throws LocationNotFound {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(91L))
                .build();

        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationPoints(points_test_parameter);
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.updateLocation(location_invalid)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void DeleteLocation_WithExsitsLocation_ReturnNull() throws LocationNotFound {
        Location location_delete = locationService.getByWork(778L);
        locationService.deleteLocation(location_delete);
        assertNull(locationRepository.findById(location_delete.getId()).orElse(null));
    }

    @Test
    void DeleteLocation_WithNonExsitsLocation_ThrowException() throws LocationNotFound {
        LocationNotFound locationNotFoundException = assertThrows(
                LocationNotFound.class,
                () -> locationService.deleteLocation(Location.builder().id(8292L).build())
        );
        assertTrue(locationNotFoundException.getMessage().contentEquals("Location with id - 8292 not found"));
    }

    @Test
    void GetWork_WIthExistsLocation_ReturnTrue() throws LocationNotFound {
        Location location_get_by_id = locationService.getByWork(778L);
        assertNotNull(location_get_by_id);
    }
    @Test
    void GetWork_WIthNonExistsLocation_ThrowException() throws LocationNotFound {
        LocationNotFound locationNotFoundException = assertThrows(
                LocationNotFound.class,
                () -> locationService.getByWork(7782L)
        );
        assertTrue(locationNotFoundException.getMessage().contentEquals("Location with work id - 7782 not found"));
    }

    @Test
    void FindLocationByCity_WithCityTest_ReturnTrue() {
        List<Location>locationByCity = locationService.findLocationByCity("test");
        assertTrue(locationByCity.size()>0);
    }

    @Test
    void FindLocationByCity_WithEmptyCity_ReturnTrue() {
        List<Location>locationByCity = locationService.findLocationByCity("");
        assertTrue(locationByCity.isEmpty());
    }

    @Test
    void FindLocationByNearby_WithMapCordsCorrect_ReturnTrue() throws LocationWithInvalidArguments {
        List<Location>location_nerby = locationService.findLocationByNearby(80,60);
        System.out.println(location_nerby);
    }

    @Test
    void FindLocationByNearby_WithMapCordsXLess0_ThrowException() throws LocationWithInvalidArguments {
         LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                 LocationWithInvalidArguments.class,
                () -> locationService.findLocationByNearby(-80,70)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Map сoordinates isn't correct!"));
    }

    @Test
    void FindLocationByNearby_WithMapCordsXMoreThan180_ThrowException() throws LocationWithInvalidArguments {
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.findLocationByNearby(800,70)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Map сoordinates isn't correct!"));
    }

    @Test
    void FindLocationByNearby_WithMapCordsYLess0_ThrowException() throws LocationWithInvalidArguments {
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.findLocationByNearby(80,-70)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Map сoordinates isn't correct!"));
    }

    @Test
    void FindLocationByNearby_WithMapCordsXMoreThan90_ThrowException() throws LocationWithInvalidArguments {
        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> locationService.findLocationByNearby(80,900)
        );
        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Map сoordinates isn't correct!"));
    }
}