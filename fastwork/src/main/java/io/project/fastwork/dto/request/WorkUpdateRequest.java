package io.project.fastwork.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkUpdateRequest {
    private Long workId;
    private String workName;
    private String workDescribe;
    private Integer workCountPerson;
    private Float workPrice;
    private Long workTypeId;
    private Long workHirerId;
}
