package io.project.fastwork.services.api;

import io.project.fastwork.domains.Work;
import io.project.fastwork.domains.WorkApplication;
import io.project.fastwork.services.exception.WorkApplicationAlreadySend;
import io.project.fastwork.services.exception.WorkApplicationNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkApplicationServiceApi {
    WorkApplication saveWorkApplication(WorkApplication savedWorkApplication) throws WorkApplicationAlreadySend;
    WorkApplication rejectedWorkApplication(Long rejectedWorkApplicationId) throws WorkApplicationNotFound;
    WorkApplication approvedWorkApplication(Long approvedWorkApplicationId) throws WorkApplicationNotFound;
    List<WorkApplication>findByWorkId(Long work_id);
    List<WorkApplication>findByWorkerid(Long worker_id);
}
