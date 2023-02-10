package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.StatusWorkApplication;
import io.project.fastwork.domains.Work;
import io.project.fastwork.domains.WorkApplication;
import io.project.fastwork.repositories.WorkAppllicationRepository;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.api.WorkApplicationServiceApi;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.WorkApplicationAlreadySend;
import io.project.fastwork.services.exception.WorkApplicationNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class WorkApplicationServiceImpl implements WorkApplicationServiceApi {

    private WorkAppllicationRepository workAppllicationRepository;
    private WorkServiceApi workService;
    private UserServiceApi userService;
    @Override
    public WorkApplication saveWorkApplication(WorkApplication savedWorkApplication) throws WorkApplicationAlreadySend {
        List<WorkApplication> workerListApplication = workAppllicationRepository.findByWorkerId(savedWorkApplication.getWorker().getId());
        WorkApplication check_work_application = workerListApplication.stream().filter(o1 -> o1.getWork().equals(savedWorkApplication.getWork())).
                findFirst().orElse(null);
        if (check_work_application == null) {
            log.info("Save work application for work with id {} and worker with id {} in {}", savedWorkApplication.getWork().getId(),
                    savedWorkApplication.getWorker().getId(), new Date());
            savedWorkApplication.setStatusWorkApplication(StatusWorkApplication.EXPECTATION);
            savedWorkApplication.setDateApplicaton(Timestamp.valueOf(LocalDateTime.now()));
            return workAppllicationRepository.save(savedWorkApplication);
        }
        log.error("Work application for work with id {} from worker with id {} already saved in {}", savedWorkApplication.getWork().getId(),
                savedWorkApplication.getWorker().getId(), new Date());
        throw new WorkApplicationAlreadySend("Work application already send!");
    }

    @Override
    public WorkApplication rejectedWorkApplication(Long rejectedWorkApplicationId) throws WorkApplicationNotFound {
        WorkApplication workApplicationById = workAppllicationRepository.getWorkApplicationById(rejectedWorkApplicationId);
        if(workApplicationById!=null){
            log.info("Update status work application with id {} on status rejected in {}",rejectedWorkApplicationId,new Date());
            workApplicationById.setStatusWorkApplication(StatusWorkApplication.REJECT);
            return workAppllicationRepository.save(workApplicationById);
        }
        log.error("Work application with id - {} not found throw exception in {}",rejectedWorkApplicationId,new Date());
        throw new WorkApplicationNotFound(String.format("Work application with id %s",rejectedWorkApplicationId));
    }

    @Override
    public WorkApplication approvedWorkApplication(Long approvedWorkApplicationId) throws WorkApplicationNotFound {
        WorkApplication workApplicationById = workAppllicationRepository.getWorkApplicationById(approvedWorkApplicationId);
        if(workApplicationById!=null){
            log.info("Update status work application with id {} on status rejected in {}",approvedWorkApplicationId,new Date());
            workApplicationById.setStatusWorkApplication(StatusWorkApplication.APPROVE);
            return workAppllicationRepository.save(workApplicationById);
        }
        log.error("Work application with id - {} not found throw exception in {}",approvedWorkApplicationId,new Date());
        throw new WorkApplicationNotFound(String.format("Work application with id %s",approvedWorkApplicationId));
    }

    @Override
    public List<WorkApplication> findByWorkId(Long work_id) {
        return null;
    }

    @Override
    public List<WorkApplication> findByWorkerid(Long worker_id) {
        return null;
    }
}
