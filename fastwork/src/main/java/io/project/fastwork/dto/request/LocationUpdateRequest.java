package io.project.fastwork.dto.request;

import io.project.fastwork.domains.Points;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateRequest {
    private Long locationId;
    private String locationCountry;
    private String locationRegion;
    private String locationCity;
    private String locationStreet;
    private Points locationPoints;
}
