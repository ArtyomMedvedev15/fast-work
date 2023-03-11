package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.StatusWorkApplication;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.domains.WorkApplication;
import io.project.fastwork.repositories.UserRepository;
import io.project.fastwork.repositories.WorkAppllicationRepository;
import io.project.fastwork.repositories.WorkRepository;
import io.project.fastwork.services.api.MailServiceApi;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.api.WorkApplicationServiceApi;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.*;
import io.project.fastwork.services.util.MailMessageBuilder;
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
public class WorkApplicationServiceImpl implements WorkApplicationServiceApi {

    private final WorkAppllicationRepository workAppllicationRepository;
    private final WorkServiceApi workService;
    private final MailServiceApi mailService;
    private final UserServiceApi userService;
    @Transactional
    @Override
    public WorkApplication saveWorkApplication(WorkApplication savedWorkApplication) throws WorkApplicationAlreadySend {
        List<WorkApplication> workerListApplication = workAppllicationRepository.findByWorkerId(savedWorkApplication.getWorker().getId());
        WorkApplication check_work_application = workerListApplication.stream().filter(o1 -> o1.getWork().getId().equals(savedWorkApplication.getWork().getId())).
                findFirst().orElse(null);
        if (check_work_application == null) {
            log.info("Save work application for work with id {} and worker with id {} in {}", savedWorkApplication.getWork().getId(),
                    savedWorkApplication.getWorker().getId(), new Date());
            String messageForHirer = MailMessageBuilder.messageBuildHirer(savedWorkApplication.getWorker().getFullName(),
                    savedWorkApplication.getWork().getWorkHirer().getFullName());
            mailService.sendMail(savedWorkApplication.getWork().getWorkHirer().getUserEmail(), "Work application",messageForHirer);
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
            String messageForWorker = MailMessageBuilder.messageBuildWorker(workApplicationById.getWorker().getFullName(),StatusWorkApplication.REJECT);
            mailService.sendMail(workApplicationById.getWorker().getUserEmail(),"Work application",messageForWorker);
            log.info("Update status work application with id {} on status rejected in {}",rejectedWorkApplicationId,new Date());
            workApplicationById.setStatusWorkApplication(StatusWorkApplication.REJECT);
            return workAppllicationRepository.save(workApplicationById);
        }
        log.error("Work application with id - {} not found throw exception in {}",rejectedWorkApplicationId,new Date());
        throw new WorkApplicationNotFound(String.format("Work application with id %s not found!",rejectedWorkApplicationId));
    }

    @Transactional
    @Override
    public WorkApplication approvedWorkApplication(Long approvedWorkApplicationId) throws WorkApplicationNotFound, WorkAlreadyAdded {
        WorkApplication workApplicationById = workAppllicationRepository.getWorkApplicationById(approvedWorkApplicationId);
        if(workApplicationById!=null){
            String messageWorker = MailMessageBuilder.messageBuildWorker(workApplicationById.getWorker().getFullName(),StatusWorkApplication.APPROVE);
            mailService.sendMail(workApplicationById.getWorker().getUserEmail(),"Work application",messageWorker);
            log.info("Update status work application with id {} on status rejected in {}",approvedWorkApplicationId,new Date());
            userService.addWorkToWorker(workApplicationById.getWork(),workApplicationById.getWorker());
            workApplicationById.setStatusWorkApplication(StatusWorkApplication.APPROVE);
            return workAppllicationRepository.save(workApplicationById);
        }
        log.error("Work application with id - {} not found throw exception in {}",approvedWorkApplicationId,new Date());
        throw new WorkApplicationNotFound(String.format("Work application with id %s not found!",approvedWorkApplicationId));
    }

    @Transactional
    @Override
    public List<WorkApplication> findByWorkId(Long work_id) throws WorkNotFound {
        Work work_is_exists = workService.getWorkById(work_id);
        if(work_is_exists!=null){
            log.info("Get all work application with work id {} in {}",work_id,new Date());
            return workAppllicationRepository.findByWorkId(work_id);
        }
        log.error("Work with id {} doen't exists, throw exception in {}",work_id,new Date());
       throw new WorkNotFound(String.format("Work with id %s not found!",work_id));
    }

    @Transactional
    @Override
    public List<WorkApplication> findByWorkerid(Long worker_id) throws WorkerNotFound, UserNotFound {
        Users user_is_exists = userService.getById(worker_id);
        if(user_is_exists!=null){
            log.info("Get all work application by worker id {} in {}",worker_id,new Date());
            return workAppllicationRepository.findByWorkerId(worker_id);
        }
        log.error("Worker with id {} doesn't exists, throw exception in {}",worker_id,new Date());
        throw new WorkerNotFound(String.format("Worker with id %s not found!",worker_id));
    }

    @Override
    public List<WorkApplication> findAllWorkApplication() {
        log.info("Get all work application in {}",new Date());
        return workAppllicationRepository.findAll();
    }
}
