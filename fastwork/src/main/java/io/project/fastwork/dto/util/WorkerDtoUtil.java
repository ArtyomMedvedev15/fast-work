package io.project.fastwork.dto.util;

import io.project.fastwork.domains.Users;
import io.project.fastwork.dto.response.WorkerResponse;

public class WorkerDtoUtil {
    public static WorkerResponse getWorkerResponse(Users worker){
        return WorkerResponse.builder()
                .id(worker.getId())
                .workerName(worker.getUserOriginalName())
                .workerSoname(worker.getUserSoname())
                .workerEmail(worker.getUserEmail())
                .build();
    }
}
