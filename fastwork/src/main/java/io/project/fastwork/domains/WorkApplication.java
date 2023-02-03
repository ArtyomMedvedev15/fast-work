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
public class WorkApplication {
    private Long id;
    private User worker;
    private Work work;
    private Timestamp dateApplicaton;
}
