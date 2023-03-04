package io.project.fastwork.dto.util;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.dto.request.WorkSaveRequest;
import io.project.fastwork.dto.response.WorkResponse;

public class WorkDtoUtil {
    public static WorkResponse getWorkResponse(WorkSaveRequest workSaveRequest) {
        return WorkResponse.builder()
                .workName(workSaveRequest.getWorkName())
                .workDescribe(workSaveRequest.getWorkDescribe())
                .workPrice(workSaveRequest.getWorkPrice())
                .workCountPerson(workSaveRequest.getWorkCountPerson())
                .workTypeId(workSaveRequest.getWorkTypeId())
                .workHirerId(workSaveRequest.getWorkHirerId()).build();
    }

    public static Work getWorkFromRequest(WorkSaveRequest workSaveRequest) {
        return Work.builder()
                .workName(workSaveRequest.getWorkName())
                .workDescribe(workSaveRequest.getWorkDescribe())
                .workPrice(workSaveRequest.getWorkPrice())
                .workCountPerson(workSaveRequest.getWorkCountPerson())
                .workType(TypeWork.builder().id(workSaveRequest.getWorkTypeId()).build())
                .workHirer(Users.builder().id(workSaveRequest.getWorkHirerId()).build())
                .build();
    }
}
