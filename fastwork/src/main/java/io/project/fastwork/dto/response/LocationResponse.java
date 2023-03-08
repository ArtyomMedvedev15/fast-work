package io.project.fastwork.dto.response;

import io.project.fastwork.domains.Points;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {
    private String locationCountry;
    private String locationRegion;
    private String locationCity;
    private String locationStreet;
    private Points locationPoints;
    private Long locationWorkId;
}
