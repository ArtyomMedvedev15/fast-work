package io.project.fastwork.dto.util;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.dto.request.WorkSaveRequest;
import io.project.fastwork.dto.request.WorkUpdateRequest;
import io.project.fastwork.dto.response.WorkResponse;

public class WorkDtoUtil {
    public static WorkResponse getWorkResponse(Work workSaved) {
        return WorkResponse.builder()
                .workId(workSaved.getId())
                .workName(workSaved.getWorkName())
                .workDescribe(workSaved.getWorkDescribe())
                .workPrice(workSaved.getWorkPrice())
                .workCountPerson(workSaved.getWorkCountPerson())
                .workTypeId(workSaved.getWorkType().getId())
                .workHirerId(workSaved.getWorkHirer().getId())
                .statusWork(workSaved.getWorkStatus())
                .datecreateWork(workSaved.getWorkDateCreate())
                .build();
    }

    public static Work getWorkFromSaveRequest(WorkSaveRequest workSaveRequest) {
        return Work.builder()
                .workName(workSaveRequest.getWorkName())
                .workDescribe(workSaveRequest.getWorkDescribe())
                .workPrice(workSaveRequest.getWorkPrice())
                .workCountPerson(workSaveRequest.getWorkCountPerson())
                .workType(TypeWork.builder().id(workSaveRequest.getWorkTypeId()).build())
                .workHirer(Users.builder().id(workSaveRequest.getWorkHirerId()).build())
                .build();
    }
    public static Work getWorkFromUpdateRequest(WorkUpdateRequest workUpdateRequest) {
        return Work.builder()
                .id(workUpdateRequest.getWorkId())
                .workName(workUpdateRequest.getWorkName())
                .workDescribe(workUpdateRequest.getWorkDescribe())
                .workPrice(workUpdateRequest.getWorkPrice())
                .workCountPerson(workUpdateRequest.getWorkCountPerson())
                .workType(TypeWork.builder().id(workUpdateRequest.getWorkTypeId()).build())
                .workHirer(Users.builder().id(workUpdateRequest.getWorkHirerId()).build())
                .build();
    }
}
