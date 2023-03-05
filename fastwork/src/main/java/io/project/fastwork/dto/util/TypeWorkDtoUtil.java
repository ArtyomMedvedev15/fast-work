package io.project.fastwork.dto.util;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.dto.request.TypeWorkSaveRequest;
import io.project.fastwork.dto.response.TypeWorkResponse;

public class TypeWorkDtoUtil {

    public static TypeWorkResponse getTypeWorkResponse(TypeWork typeWork){
        return TypeWorkResponse.builder()
                .id(typeWork.getId())
                .typeWorkName(typeWork.getTypeWorkName())
                .typeWorkDescribe(typeWork.getTypeWorkDescribe())
                .build();
    }

    public static TypeWork getTypeWorkFromRequest(TypeWorkSaveRequest typeWorkSaveRequest){
        return TypeWork.builder()
                .typeWorkName(typeWorkSaveRequest.getTypeWorkName())
                .typeWorkDescribe(typeWorkSaveRequest.getTypeWorkDescribe())
                .build();
    }
}
