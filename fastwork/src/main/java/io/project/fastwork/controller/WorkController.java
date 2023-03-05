package io.project.fastwork.controller;

import io.project.fastwork.controller.exception.RestTypeWorkNotFoundException;
import io.project.fastwork.controller.exception.RestWorkAlreadyExistsException;
import io.project.fastwork.controller.exception.RestWorkInvalidDataValuesException;
import io.project.fastwork.controller.exception.RestWorkNotFoundException;
import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Work;
import io.project.fastwork.dto.request.WorkByNameRequest;
import io.project.fastwork.dto.request.WorkByTypeRequest;
import io.project.fastwork.dto.request.WorkSaveRequest;
import io.project.fastwork.dto.request.WorkUpdateRequest;
import io.project.fastwork.dto.response.WorkResponse;
import io.project.fastwork.dto.util.WorkDtoUtil;
import io.project.fastwork.services.api.TypeWorkServiceApi;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.TypeWorkNotFound;
import io.project.fastwork.services.exception.WorkAlreadyExists;
import io.project.fastwork.services.exception.WorkInvalidDataValues;
import io.project.fastwork.services.exception.WorkNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static io.project.fastwork.dto.util.WorkDtoUtil.*;

@RestController
@RequestMapping("/api/v1/work")
@RequiredArgsConstructor
@Slf4j
public class WorkController {
    private final WorkServiceApi workService;
    private final TypeWorkServiceApi typeWorkService;

    @GetMapping("/all")
    public ResponseEntity<?>getAll(){
        List<WorkResponse>workList=workService.findAllWork().stream().map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @GetMapping("/openedwork")
    public ResponseEntity<?>getAllWorksOpen(){
        List<WorkResponse>workList=workService.findAllOpenedWork().stream().map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @GetMapping("/closedwork")
    public ResponseEntity<?>getAllClosedWork(){
        List<WorkResponse>workList=workService.findAllClosedWork().stream().map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @GetMapping("/exceptionwork")
    public ResponseEntity<?>getAllExceptionWork(){
        List<WorkResponse>workList=workService.findAllExceptionWork().stream().map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @GetMapping("/{id_work}")
    public ResponseEntity<?>getAllWorks(@PathVariable("id_work")Long id_work){
        Work work_by_id;
        try {
            work_by_id = workService.getWorkById(id_work);
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found",id_work));
        }
        WorkResponse workResponseById = getWorkResponse(work_by_id);
        return ResponseEntity.ok().body(workResponseById);
    }

    @PostMapping("/findbyname")
    public ResponseEntity<?>getAllWorksByName(@RequestBody WorkByNameRequest workByNameRequest){
        List<WorkResponse>workList = workService.findWorkByName(workByNameRequest.getWorkname()).stream()
                .map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @PostMapping("/findbytype")
    public ResponseEntity<?>getAllWorkByType(@RequestBody WorkByTypeRequest workByTypeRequest){
        TypeWork typeWorkById;
        try {
            typeWorkById = typeWorkService.getTypeWorkById(workByTypeRequest.getId_type());
        } catch (TypeWorkNotFound e) {
            throw new RestTypeWorkNotFoundException(String.format("Type work with id %s not found",workByTypeRequest.getId_type()));
        }
        List<WorkResponse>workList = workService.findWorkByTypeWork(typeWorkById).stream()
                .map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @PostMapping("/save")
    public ResponseEntity<?>saveWork(@RequestBody WorkSaveRequest workSaveRequest){
        Work work_save = getWorkFromSaveRequest(workSaveRequest);
        try {
            work_save = workService.saveWork(work_save);
        } catch (WorkAlreadyExists e) {
            throw new RestWorkAlreadyExistsException(String.format("Work with name %s already exists!",workSaveRequest.getWorkName()));
        } catch (WorkInvalidDataValues e) {
            throw new RestWorkInvalidDataValuesException("Work data isn't correct, try yet!");
        }
        WorkResponse workResponse = getWorkResponse(work_save);
        return ResponseEntity.ok().body(workResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<?>updateWork(@RequestBody WorkUpdateRequest workUpdateRequest){
        Work work_update = getWorkFromUpdateRequest(workUpdateRequest);
        try {
            Work work_old = workService.getWorkById(workUpdateRequest.getWorkId());
            work_update.setWorkStatus(work_old.getWorkStatus());
            work_update.setWorkDateCreate(work_old.getWorkDateCreate());
            work_update = workService.updateWork(work_update);
        } catch (WorkInvalidDataValues e) {
            throw new RestWorkInvalidDataValuesException("Work data isn't correct, try yet!");
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found",workUpdateRequest.getWorkTypeId()));
        }
        WorkResponse workResponse = getWorkResponse(work_update);
        return ResponseEntity.ok().body(workResponse);
    }

    @PostMapping("/closework/{id_work}")
    public ResponseEntity<?>closeWork(@PathVariable("id_work")Long id_work){
        Work work_closed;
        try {
            work_closed = workService.closeWork(workService.getWorkById(id_work));
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found",id_work));
        }
        WorkResponse workResponse = getWorkResponse(work_closed);
        return ResponseEntity.ok().body(workResponse);
    }

    @PostMapping("/exceptionwork/{id_work}")
    public ResponseEntity<?>exceptionWork(@PathVariable("id_work")Long id_work){
        Work work_exception;
        try {
            work_exception = workService.exceptionWork(workService.getWorkById(id_work));
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found",id_work));
        }
        WorkResponse workResponse = getWorkResponse(work_exception);
        return ResponseEntity.ok().body(workResponse);
    }

    @PostMapping("/openwork/{id_work}")
    public ResponseEntity<?>openWork(@PathVariable("id_work")Long id_work){
        Work work_open;
        try {
            work_open = workService.openWork(workService.getWorkById(id_work));
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found",id_work));
        }
        WorkResponse workResponse = getWorkResponse(work_open);
        return ResponseEntity.ok().body(workResponse);
    }




}
