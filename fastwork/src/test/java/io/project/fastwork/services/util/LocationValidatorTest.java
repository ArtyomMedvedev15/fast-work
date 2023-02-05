package io.project.fastwork.services.util;

import io.project.fastwork.domains.Location;
import io.project.fastwork.domains.Points;
import io.project.fastwork.services.exception.LocationWithInvalidArguments;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationValidatorTest {
    @Test
    void LocationTest_WithValidParaments_ReturnTrue() throws LocationWithInvalidArguments {
        Points points_test_parameter = Points.builder()
                .x(BigDecimal.valueOf(123L))
                .y(BigDecimal.valueOf(87L))
                .build();
        Location location_valid = Location.builder()
                .locationCity("Test")
                .locationCountry("Test")
                .locationRegion("Test")
                .locationStreet("Test")
                .locationPoints(points_test_parameter)
                .build();

        boolean is_valid_location = LocationValidator.LocationValidDataValues(location_valid);

        assertTrue(is_valid_location);

    }

    @Test
    void LocationTest_WithInvalidCity_ReturnTrue() {
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

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void LocationTest_WithInvalidCountry_ReturnTrue() {
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

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_country)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_region)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
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

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }

    @Test
    void LocationTest_WithInvalidMapCordYMore90_ReturnTrue() {
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

        LocationWithInvalidArguments locationWithInvalidArguments = assertThrows(
                LocationWithInvalidArguments.class,
                () -> LocationValidator.LocationValidDataValues(location_with_invalid_city)
        );

        assertTrue(locationWithInvalidArguments.getMessage().contentEquals("Check string parameters and location x and y, something was wrong"));
    }
}