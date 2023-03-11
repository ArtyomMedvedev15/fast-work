package io.project.fastwork.dto.response;

import io.project.fastwork.domains.StatusWorkApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkApplicationResponse {
    private Long id;
    private WorkerResponse worker;
    private WorkResponse work;
    private Timestamp dateApplicaton;
    private StatusWorkApplication statusWorkApplication;
}
