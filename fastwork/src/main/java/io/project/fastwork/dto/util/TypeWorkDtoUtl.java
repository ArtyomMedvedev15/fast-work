package io.project.fastwork.dto.util;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.dto.response.TypeWorkResponse;

public class TypeWorkDtoUtl {

    public static TypeWorkResponse getTypeWorkResponse(TypeWork typeWork){
        return TypeWorkResponse.builder()
                .id(typeWork.getId())
                .typeWorkName(typeWork.getTypeWorkName())
                .typeWorkDescribe(typeWork.getTypeWorkDescribe())
                .build();
    }
}
