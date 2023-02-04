package io.project.fastwork.domains;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String locationCountry;
    private String locationRegion;
    private String locationCity;
    private String locationStreet;
    @Embedded
    private Points locationPoints;
    private Timestamp locationDateCreate;
    @OneToOne
    private Work locationWork;
}
