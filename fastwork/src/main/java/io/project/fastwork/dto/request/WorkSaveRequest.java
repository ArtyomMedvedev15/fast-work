package io.project.fastwork.dto.request;

import io.project.fastwork.domains.StatusWork;
import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Users;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkSaveRequest {
    private String workName;
    private String workDescribe;
    private Integer workCountPerson;
    private Float workPrice;
    private Long workTypeId;
    private Long workHirerId;
}
