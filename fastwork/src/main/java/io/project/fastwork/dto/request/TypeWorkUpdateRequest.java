package io.project.fastwork.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeWorkUpdateRequest {
    private Long id;
    private String typeWorkName;
    private String typeWorkDescribe;
}
