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
public class TypeWork {
    private Long id;
    private String typeWorkName;
    private String typeWorkDescribe;
    private Timestamp typeWorkDateCreate;
}
