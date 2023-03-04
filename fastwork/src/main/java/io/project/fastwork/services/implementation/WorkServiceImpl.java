package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.StatusWork;
import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Work;
import io.project.fastwork.repositories.TypeWorkRepository;
import io.project.fastwork.repositories.WorkRepository;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.TypeWorkNotFound;
import io.project.fastwork.services.exception.WorkAlreadyExists;
import io.project.fastwork.services.exception.WorkInvalidDataValues;
import io.project.fastwork.services.exception.WorkNotFound;
import io.project.fastwork.services.util.WorkValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class WorkServiceImpl implements WorkServiceApi {

    private final WorkRepository workRepository;
    private final TypeWorkRepository typeWorkRepository;


    @Override
    public Work saveWork(Work savedWork) throws WorkAlreadyExists, WorkInvalidDataValues {
        Work check_work_exists = workRepository.findAll().stream().filter(o1 -> o1.getWorkName().equals(savedWork.getWorkName()))
                .findFirst().orElse(null);

        if (check_work_exists != null) {
            log.error("Work with name {} already exists, try yet in {}", savedWork.getWorkName(), new Date());
            throw new WorkAlreadyExists(String.format("Work with name - %s already exists, try yet", savedWork.getWorkName()));
        }
        if (WorkValidator.WorkValidDataValues(savedWork)) {
            log.info("Save new work with name - {} in {}", savedWork.getWorkName(), new Date());
            savedWork.setWorkDateCreate(Timestamp.valueOf(LocalDateTime.now()));
            return workRepository.save(savedWork);
        } else {
            log.error("Invalid work data parameter throw exception in {}", new Date());
            throw new WorkInvalidDataValues("Invalid work data parameter, check parameter");
        }
    }

    @Override
    public Work updateWork(Work updatedWork) throws WorkInvalidDataValues {
        if (WorkValidator.WorkValidDataValues(updatedWork)) {
            log.info("Update work with id {} in {}", updatedWork.getId(), new Date());
            return workRepository.save(updatedWork);
        } else {
            log.error("Invalid work data parameter throw exception in {}", new Date());
            throw new WorkInvalidDataValues("Invalid work data parameter, check parameter");
        }
    }

    @Override
    public Work closeWork(Work closedWork) throws WorkNotFound {
        Work check_work_exists = workRepository.getWorkById(closedWork.getId());
        if (check_work_exists != null) {
            log.info("Set work with id {} status close in {}", closedWork.getId(), new Date());
            return workRepository.workChangeStatus(StatusWork.CLOSE, closedWork.getId());
        } else {
            log.error("Work with id {} was not found throw exception in {}", closedWork.getId(), new Date());
            throw new WorkNotFound(String.format("Work with id %s not found!", closedWork.getId()));
        }
    }

    @Override
    public Work openWork(Work openedWork) throws WorkNotFound {
        Work check_work_exists = workRepository.getWorkById(openedWork.getId());
        if (check_work_exists != null) {
            log.info("Set work with id {} status open in {}", openedWork.getId(), new Date());
            return workRepository.workChangeStatus(StatusWork.OPEN, openedWork.getId());
        } else {
            log.error("Work with id {} was not found throw exception in {}", openedWork.getId(), new Date());
            throw new WorkNotFound(String.format("Work with id %s not found!", openedWork.getId()));
        }
    }

    @Override
    public Work getWorkById(Long work_id) throws WorkNotFound {
        Work check_work_exists = workRepository.getWorkById(work_id);
        if (check_work_exists != null) {
            log.info("Get work with id {} in {}", work_id, new Date());
            return workRepository.getWorkById(work_id);
        } else {
            log.error("Work with id {} was not found throw exception in {}", work_id, new Date());
            throw new WorkNotFound(String.format("Work with id %s not found!", work_id));
        }
    }

    @Override
    public Work exceptionWork(Work exceptionWork) throws WorkNotFound {
        Work check_work_exists = workRepository.getWorkById(exceptionWork.getId());
        if (check_work_exists != null) {
            log.info("Set work with id {} status exception in {}", exceptionWork.getId(), new Date());
            return workRepository.workChangeStatus(StatusWork.EXPECTATION, exceptionWork.getId());
        } else {
            log.error("Work with id {} was not found throw exception in {}", exceptionWork.getId(), new Date());
            throw new WorkNotFound(String.format("Work with id %s not found!", exceptionWork.getId()));
        }
    }

    @Override
    public List<Work> findWorkByName(String nameWork) {
        if (!nameWork.isEmpty()) {
            log.info("Get work by name {} in {}", nameWork, new Date());
            return workRepository.findWorkByName(nameWork).stream().filter(o1->o1.getWorkStatus().equals(StatusWork.OPEN)).collect(Collectors.toList());
        } else {
            log.warn("Get all work, work name equals empty, in {}", new Date());
            return workRepository.findAll();
        }
    }

    @Transactional
    @Override
    public List<Work> findWorkByTypeWork(TypeWork typeWork){
        TypeWork check_type_work_exists = typeWorkRepository.findByTypeWorkName(typeWork.getTypeWorkName());
        if (check_type_work_exists != null) {
            log.info("Get work with type work {} in {}", typeWork.getTypeWorkName(), new Date());
            return workRepository.findWorkByType(typeWork.getId()).stream().filter(o1->o1.getWorkStatus().equals(StatusWork.OPEN)).collect(Collectors.toList());
        } else {
            log.error("Cannot get work by type, type work equals null, in {}", new Date());
            return Collections.emptyList();
        }
    }

    @Override
    public List<Work> findAllOpenedWork() {
        log.info("Get all work with status open in {}", new Date());
        return workRepository.findAll().stream().filter(o1 -> o1.getWorkStatus().equals(StatusWork.OPEN))
                .collect(Collectors.toList());
    }

    @Override
    public List<Work> findAllClosedWork() {
        log.info("Get all work with status close in {}", new Date());
        return workRepository.findAll().stream().filter(o1 -> o1.getWorkStatus().equals(StatusWork.CLOSE))
                .collect(Collectors.toList());
    }

    @Override
    public List<Work> findAllExceptionWork() {
        log.info("Get all work with status exception in {}", new Date());
        return workRepository.findAll().stream().filter(o1 -> o1.getWorkStatus().equals(StatusWork.EXPECTATION))
                .collect(Collectors.toList());
    }
}
