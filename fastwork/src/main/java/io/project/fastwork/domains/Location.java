package io.project.fastwork.domains;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode
@ToString
@Builder
public class Location {
    private Long id;
    private String locationCountry;
    private String locationRegion;
    private String locationCity;
    private String locationStreet;
    private Points locationPoints;
    private Timestamp locationDateCreate;
}
