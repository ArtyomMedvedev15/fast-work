package io.project.fastwork.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeWorkResponse {
    private Long id;
    private String typeWorkName;
    private String typeWorkDescribe;
}
