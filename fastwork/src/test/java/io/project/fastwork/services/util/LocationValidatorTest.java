package io.project.fastwork.services.util;

import io.project.fastwork.domains.Location;
import io.project.fastwork.domains.Points;
import io.project.fastwork.services.exception.LocationWithInvalidArgumentsException;
import io.project.fastwork.services.exception.UserInvalidDataParemeter;
import io.project.fastwork.util.LocationArgumentProviders;
import io.project.fastwork.util.UserArgumentProviders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationValidatorTest {
    @ParameterizedTest
    @ArgumentsSource(LocationArgumentProviders.LocationArgumentInvalidProviders.class)
    void LocationTest_WithValidParaments_ReturnTrue(Location location_invalid) throws LocationWithInvalidArgumentsException {
        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_invalid)
        );
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void LocationTest_WithInvalidCity_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_city = Location.builder()
                .locationCity("123")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void LocationTest_WithInvalidCityLess4Length_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_city = Location.builder()
                .locationCity("Tes")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void LocationTest_WithInvalidCityMore30Length_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_city = Location.builder()
                .locationCity("TestTestTestTestTestTestTestTest")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void LocationTest_WithInvalidCountry_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_country = Location.builder()
                .locationCity("Test")
                .locationCountry("123")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_country)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void LocationTest_WithInvalidCountryLess4Length_ThrowException() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_country = Location.builder()
                .locationCity("Test")
                .locationCountry("Tes")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_country)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void LocationTest_WithInvalidCountryMoreThan30Length_ThrowExcepti() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_country = Location.builder()
                .locationCity("Test")
                .locationCountry("TestTestTestTestTestTestTestTest")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_country)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void LocationTest_WithInvalidRegion_ReturnTrue() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_region = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("123")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_region)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void LocationTest_WithInvalidRegionLess4Lentgh_ReturnTrue() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_region = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Tes")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_region)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void LocationTest_WithInvalidRegionMoreThan45Lentgh_ReturnTrue() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_region = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("TestTestTestTestTestTestTestTestTestTestTestTest")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_region)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void LocationTest_WithInvalidStreet_ReturnTrue() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_city = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("123")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void LocationTest_WithInvalidStreetLess4Length_ReturnTrue() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_city = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Tes")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void LocationTest_WithInvalidStreetMoreThan45Length_ReturnTrue() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_city = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("TestTestTestTestTestTestTestTestTestTestTestTest")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );
        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
    @Test
    void LocationTest_WithInvalidMapCordXLess0_ReturnTrue() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(-12L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_city = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void LocationTest_WithInvalidMapCordXMoreThan180_ReturnTrue() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(181L))
                .y(BigDecimal.valueOf(87L))
                .build();

        Location location_with_invalid_city = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void LocationTest_WithInvalidMapCordYLess0_ReturnTrue() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(181L))
                .y(BigDecimal.valueOf(-87L))
                .build();

        Location location_with_invalid_city = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void LocationTest_WithInvalidMapCordYMore90_ReturnTrue() {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(181L))
                .y(BigDecimal.valueOf(91))
                .build();

        Location location_with_invalid_city = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        LocationWithInvalidArgumentsException locationWithInvalidArgumentsException = assertThrows(
                LocationWithInvalidArgumentsException.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArgumentsException.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
}