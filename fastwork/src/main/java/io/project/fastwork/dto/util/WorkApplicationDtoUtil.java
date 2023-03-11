package io.project.fastwork.dto.util;

import io.project.fastwork.domains.WorkApplication;
import io.project.fastwork.dto.response.WorkApplicationResponse;

public class WorkApplicationDtoUtil {
    public static WorkApplicationResponse getWorkApplicationRepsonse(WorkApplication workApplication){
        return WorkApplicationResponse.builder()
                .id(workApplication.getId())
                .worker(WorkerDtoUtil.getWorkerResponse(workApplication.getWorker()))
                .work(WorkDtoUtil.getWorkResponse(workApplication.getWork()))
                .dateApplicaton(workApplication.getDateApplicaton())
                .statusWorkApplication(workApplication.getStatusWorkApplication())
                .build();
    }
}
