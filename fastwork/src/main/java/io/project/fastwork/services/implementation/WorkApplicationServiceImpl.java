package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.Work;
import io.project.fastwork.domains.WorkApplication;
import io.project.fastwork.repositories.WorkAppllicationRepository;
import io.project.fastwork.services.api.WorkApplicationServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkApplicationServiceImpl implements WorkApplicationServiceApi {

    private WorkAppllicationRepository workAppllicationRepository;

    @Override
    public WorkApplication saveWorkApplication(WorkApplication savedWorkApplication) {
        return null;
    }

    @Override
    public WorkApplication rejectedWorkApplication(WorkApplication rejectedWorkApplication) {
        return null;
    }

    @Override
    public WorkApplication approvedWorkApplication(WorkApplication approvedWorkApplication) {
        return null;
    }

    @Override
    public List<WorkApplication> findAllWorkApplicationByWork(Work work) {
        return null;
    }
}
