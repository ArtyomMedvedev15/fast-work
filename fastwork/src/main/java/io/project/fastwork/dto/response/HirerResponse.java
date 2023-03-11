package io.project.fastwork.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HirerResponse {
    private Long id;
    private String workerName;
    private String workerSoname;
    private String workerEmail;
}
