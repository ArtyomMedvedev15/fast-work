package io.project.fastwork.services.api;

import io.project.fastwork.domains.WorkApplication;
import io.project.fastwork.services.exception.*;

import java.util.List;


public interface WorkApplicationServiceApi {
    WorkApplication saveWorkApplication(WorkApplication savedWorkApplication) throws WorkApplicationAlreadySend;
    WorkApplication rejectedWorkApplication(Long rejectedWorkApplicationId) throws WorkApplicationNotFound;
    WorkApplication approvedWorkApplication(Long approvedWorkApplicationId) throws WorkApplicationNotFound, WorkAlreadyAdded;
    List<WorkApplication>findByWorkId(Long work_id) throws WorkNotFound;
    List<WorkApplication>findByWorkerid(Long worker_id) throws WorkerNotFound, UserNotFound;
    List<WorkApplication>findAllWorkApplication();
}
