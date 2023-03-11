package io.project.fastwork.dto.response;

import io.project.fastwork.domains.StatusWork;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkResponse {
    private Long workId;
    private String workName;
    private String workDescribe;
    private Integer workCountPerson;
    private Float workPrice;
    private Long workTypeId;
    private HirerResponse workHirer;
    private StatusWork statusWork;
    private Timestamp datecreateWork;
}
