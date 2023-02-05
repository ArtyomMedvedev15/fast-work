package io.project.fastwork.services;

import io.project.fastwork.domains.Work;
import io.project.fastwork.domains.WorkApplication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkApplicationServiceApi {
    WorkApplication saveWorkApplication(WorkApplication savedWorkApplication);
    WorkApplication rejectedWorkApplication(WorkApplication rejectedWorkApplication);
    WorkApplication approvedWorkApplication(WorkApplication approvedWorkApplication);
    List<WorkApplication>findAllWorkApplicationByWork(Work work);
}
