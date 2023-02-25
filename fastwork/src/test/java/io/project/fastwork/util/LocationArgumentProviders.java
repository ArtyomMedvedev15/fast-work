package io.project.fastwork.util;

import io.project.fastwork.domains.Location;
import io.project.fastwork.domains.Points;
import io.project.fastwork.domains.Work;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class LocationArgumentProviders {
    static public class LocationArgumentInvalidProviders implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of(Location.builder()
                                    .locationCity("123")
                                    .locationCountry("Test")
                                    .locationRegion("Test")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Tes")
                                    .locationCountry("Test")
                                    .locationRegion("Test")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("TestTestTestTestTestTestTestTest")
                                    .locationCountry("Test")
                                    .locationRegion("Test")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("123")
                                    .locationRegion("Test")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Tes")
                                    .locationRegion("Test")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("TestTestTestTestTestTestTestTest")
                                    .locationRegion("Test")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Test")
                                    .locationRegion("123")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Test")
                                    .locationRegion("Tes")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Test")
                                    .locationRegion("TestTestTestTestTestTestTestTestTestTestTestTest")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Test")
                                    .locationRegion("Test")
                                    .locationStreet("123")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Test")
                                    .locationRegion("Test")
                                    .locationStreet("Tes")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Test")
                                    .locationRegion("Test")
                                    .locationStreet("TestTestTestTestTestTestTestTestTestTestTestTest")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Test")
                                    .locationRegion("Test")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(-12L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Test")
                                    .locationRegion("Test")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(181L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Test")
                                    .locationRegion("Test")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(181L))
                                            .y(BigDecimal.valueOf(-87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Test")
                                    .locationRegion("Test")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(181L))
                                            .y(BigDecimal.valueOf(91))
                                            .build())
                                    .build()
                    ));
        }
    }

    static public class LocationArgumentValidProviders implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of(Location.builder()
                                    .locationCity("Test")
                                    .locationCountry("Test")
                                    .locationRegion("Test")
                                    .locationStreet("Test")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Location 2")
                                    .locationCountry("BY")
                                    .locationRegion("MK")
                                    .locationStreet("Centr")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build(),
                            Location.builder()
                                    .locationCity("Location 3")
                                    .locationCountry("BY")
                                    .locationRegion("MK")
                                    .locationStreet("Centr")
                                    .locationPoints(Points.builder()
                                            .x(BigDecimal.valueOf(123L))
                                            .y(BigDecimal.valueOf(87L))
                                            .build())
                                    .build()
                    ));
        }
    }
}
