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
public class Work {
    private Long id;
    private String workName;
    private String workDescribe;
    private Integer workCountPerson;
    private Float workPrice;
    private Location workLocation;
    private Timestamp workDateCreate;
    private StatusWork workStatus;
    private User workHirer;
}
