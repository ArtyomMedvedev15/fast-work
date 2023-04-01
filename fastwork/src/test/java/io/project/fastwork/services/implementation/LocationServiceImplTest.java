package io.project.fastwork.services.implementation;

import io.project.fastwork.configuration.MailConfig;
import io.project.fastwork.domains.Location;
import io.project.fastwork.domains.Points;
import io.project.fastwork.domains.Work;
import io.project.fastwork.repositories.LocationRepository;
import io.project.fastwork.services.api.LocationServiceApi;
import io.project.fastwork.services.api.MailServiceApi;
import io.project.fastwork.services.exception.LocationNotFoundException;
import io.project.fastwork.services.exception.LocationWithInvalidArgumentsException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LocationServiceImplTest {

    @Autowired
    private LocationServiceApi locationService;

    @MockBean
    private LocationRepository locationRepository;
    @Test
    void SaveLocation_WithWorkId777_ReturnTrue() throws LocationWithInvalidArgumentsException, LocationNotFoundException {
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

        Mockito.when(locationRepository.save(location_valid)).thenReturn(location_valid);

        Location locaiton_saved = locationService.saveLocation(location_valid);

        Mockito.verify(locationRepository, Mockito.times(1)).save(location_valid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);

        assertEquals("Test", locaiton_saved.getLocationCity());
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
        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_invalid);
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );

        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Invalid data for location, or try to save second location for work, throw exception"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.saveLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_invalid);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(777L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithCorrectCity_ReturnTrue() throws LocationNotFoundException, LocationWithInvalidArgumentsException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();
        Mockito.when(locationRepository.getReferenceById(778L)).thenReturn(location_by_id);
        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Mockito.when(locationRepository.save(location_by_id)).thenReturn(location_by_id);

        Location location_valid = locationService.getByWork(778L);
        location_valid.setLocationCity("Minsk");
        locationService.updateLocation(location_valid);

        Mockito.verify(locationRepository, Mockito.times(1)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(1)).getReferenceById(778L);
        assertEquals("Minsk", locationService.getByWork(778L).getLocationCity());
    }

    @Test
    void UpdateLocation_WithInCorrectCityLengthLess4_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);

        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationCity("Min");
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );

        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithInCorrectCityLengthMore30_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationCity("MinskMinskMinskMinskMinskMinskMinsk");
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );

        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithInCorrectCountryLengthLess4_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationCountry("Bel");
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithInCorrectCountryLengthMore30_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationCountry("MinskMinskMinskMinskMinskMinskMinsk");
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithInCorrectRegionLengthLess4_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);

        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationRegion("Bel");
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );

        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithInCorrectRegionLengthMore40_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationCountry("MinskMinskMinskMinskMinskMinskMinskMinskMinsk");
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );

        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithInCorrectStreetLengthLess4_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationStreet("Bel");
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithInCorrectStreetLengthMore40_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationStreet("MinskMinskMinskMinskMinskMinskMinskMinskMinskMinsk");
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );

        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithIncorrectMapCordXLess0_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(-123L))
                .y(BigDecimal.valueOf(91L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationPoints(points_test_parameter);
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithIncorrectMapCordXMoreThan180_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(1223L))
                .y(BigDecimal.valueOf(91L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationPoints(points_test_parameter);
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithIncorrectMapCordYLess0_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(-91L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationPoints(points_test_parameter);
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void UpdateLocation_WithIncorrectMapCordYMoreThan90_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(91L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(777L).build())
                .locationPoints(points_test_parameter)
                .build();
        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Location location_invalid = locationService.getByWork(778L);
        location_invalid.setLocationPoints(points_test_parameter);
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.updateLocation(location_invalid)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).save(location_by_id);
        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        Mockito.verify(locationRepository, Mockito.times(0)).getReferenceById(778L);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void DeleteLocation_WithExsitsLocation_ReturnNull() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .id(778L)
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(778L).build())
                .locationPoints(points_test_parameter)
                .build();
        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Mockito.when(locationRepository.findById(778L)).thenReturn(Optional.of(location_by_id));
        Location location_delete = locationService.getByWork(778L);
        locationService.deleteLocation(location_delete);

        Mockito.when(locationRepository.findById(778L)).thenReturn(Optional.empty());

        Mockito.verify(locationRepository, Mockito.times(1)).findById(778L);
        Mockito.verify(locationRepository, Mockito.times(1)).delete(location_delete);
        assertNull(locationRepository.findById(location_delete.getId()).orElse(null));
    }

    @Test
    void DeleteLocation_WithNonExsitsLocation_ThrowException() throws LocationNotFoundException {
        Mockito.when(locationRepository.findById(8292L)).thenReturn(Optional.empty());

        LocationNotFoundException locationNotFoundException = assertThrows(
                LocationNotFoundException.class,
                () -> locationService.deleteLocation(Location.builder().id(8292L).build())
        );

        Mockito.verify(locationRepository, Mockito.times(1)).findById(8292L);
        Mockito.verify(locationRepository, Mockito.times(0)).delete(Location.builder().id(8292L).build());
        assertTrue(locationNotFoundException.getMessage().contentEquals("Location with id - 8292 not found"));
    }

    @Test
    void GetWork_WIthExistsLocation_ReturnTrue() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(778L).build())
                .locationPoints(points_test_parameter)
                .build();
        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        Location location_get_by_id = locationService.getByWork(778L);

        Mockito.verify(locationRepository, Mockito.times(1)).getByWorkId(778L);
        assertNotNull(location_get_by_id);
    }

    @Test
    void GetWork_WIthNonExistsLocation_ThrowException() throws LocationNotFoundException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_by_id = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(778L).build())
                .locationPoints(points_test_parameter)
                .build();
        Mockito.when(locationRepository.getByWorkId(778L)).thenReturn(location_by_id);
        LocationNotFoundException locationNotFoundException = assertThrows(
                LocationNotFoundException.class,
                () -> locationService.getByWork(7782L)
        );

        assertTrue(locationNotFoundException.getMessage().contentEquals("Location with work id - 7782 not found"));
    }

    @Test
    void FindLocationByCity_WithCityTest_ReturnTrue() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(778L).build())
                .locationPoints(points_test_parameter)
                .build();

        Mockito.when(locationRepository.findLocationByCity("test")).thenReturn(List.of(location));
        List<Location> locationByCity = locationService.findLocationByCity("test");

        Mockito.verify(locationRepository, Mockito.times(1)).findLocationByCity("test");
        assertTrue(locationByCity.size() > 0);
    }

    @Test
    void FindLocationByCity_WithEmptyCity_ReturnTrue() {
        Mockito.when(locationRepository.findLocationByCity("")).thenReturn(Collections.emptyList());
        List<Location> locationByCity = locationService.findLocationByCity("");
        Mockito.verify(locationRepository, Mockito.times(0)).findLocationByCity("");
        assertTrue(locationByCity.isEmpty());
    }

    @Test
    @Transactional
    void FindLocationByNearby_WithMapCordsCorrect_ReturnTrue() throws LocationWithInvalidArgumentsException {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(70L))
                .y(BigDecimal.valueOf(60L))
                .build();
        Location location = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationWork(Work.builder().id(778L).build())
                .locationPoints(points_test_parameter)
                .build();
        Mockito.when(locationRepository.findLocationByNearby(80, 60)).thenReturn(List.of(location));
        List<Location> location_nerby = locationService.findLocationByNearby(80, 60);
        Mockito.verify(locationRepository, Mockito.times(1)).findLocationByNearby(80, 60);
        System.out.println(location_nerby);
    }

    @Test
    void FindLocationByNearby_WithMapCordsXLess0_ThrowException() throws LocationWithInvalidArgumentsException {
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.findLocationByNearby(-80, 70)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).findLocationByNearby(-80, 70);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Map сoordinates isn't correct!"));
    }

    @Test
    void FindLocationByNearby_WithMapCordsXMoreThan180_ThrowException() throws LocationWithInvalidArgumentsException {
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.findLocationByNearby(800, 70)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).findLocationByNearby(800, 70);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Map сoordinates isn't correct!"));
    }

    @Test
    void FindLocationByNearby_WithMapCordsYLess0_ThrowException() throws LocationWithInvalidArgumentsException {
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.findLocationByNearby(80, -70)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).findLocationByNearby(80, -70);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Map сoordinates isn't correct!"));
    }

    @Test
    void FindLocationByNearby_WithMapCordsXMoreThan90_ThrowException() throws LocationWithInvalidArgumentsException {
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> locationService.findLocationByNearby(80, 900)
        );
        Mockito.verify(locationRepository, Mockito.times(0)).findLocationByNearby(80, 900);
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Map сoordinates isn't correct!"));
    }
}