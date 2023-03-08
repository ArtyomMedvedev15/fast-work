package io.project.fastwork.dto.util;

import io.project.fastwork.domains.Location;
import io.project.fastwork.dto.response.LocationResponse;

public class LocationDtoUtil {
    public static LocationResponse getLocationResponseFromDomain(Location location){
        return LocationResponse.builder()
                .locationCountry(location.getLocationCountry())
                .locationRegion(location.getLocationRegion())
                .locationCity(location.getLocationCity())
                .locationStreet(location.getLocationStreet())
                .locationPoints(location.getLocationPoints())
                .locationWorkId(location.getLocationWork().getId())
                .build();
    }
}
