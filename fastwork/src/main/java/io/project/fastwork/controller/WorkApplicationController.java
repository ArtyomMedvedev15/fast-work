package io.project.fastwork.controller;

import io.project.fastwork.controller.advice.exception.*;
import io.project.fastwork.domains.WorkApplication;
import io.project.fastwork.dto.request.WorkApplicationSaveRequest;
import io.project.fastwork.dto.response.WorkApplicationResponse;
import io.project.fastwork.dto.util.WorkApplicationDtoUtil;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.api.WorkApplicationServiceApi;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workapplication")
@RequiredArgsConstructor
@Slf4j
public class WorkApplicationController {
    private final WorkApplicationServiceApi workApplicationService;
    private final WorkServiceApi workService;
    private final UserServiceApi userService;
    @GetMapping("/all")
    public ResponseEntity<?>getAllWorkApplication(){
        List<WorkApplicationResponse>workApplicationResponses= workApplicationService.findAllWorkApplication().stream()
                .map(WorkApplicationDtoUtil::getWorkApplicationRepsonse).toList();
        return ResponseEntity.ok().body(workApplicationResponses);
    }

    @GetMapping("/findbywork/{id_work}")
    public ResponseEntity<?>getAllWorkApplicationByWorkId(@PathVariable("id_work")Long id_work){
        List<WorkApplicationResponse>workApplicationResponsesByWorkId;
        try {
            workApplicationResponsesByWorkId=workApplicationService.findByWorkId(id_work).stream()
                    .map(WorkApplicationDtoUtil::getWorkApplicationRepsonse).toList();
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found",id_work));
        }
        return ResponseEntity.ok().body(workApplicationResponsesByWorkId);
    }

    @GetMapping("/findbyworker/{id_worker}")
    public ResponseEntity<?>getAllWorkApplicationByWorkerId(@PathVariable("id_worker")Long id_worker){
        List<WorkApplicationResponse>workApplicationResponseList;
        try {
            workApplicationResponseList = workApplicationService.findByWorkerid(id_worker).stream()
                    .map(WorkApplicationDtoUtil::getWorkApplicationRepsonse).toList();
        } catch (WorkerNotFound | UserNotFound e) {
            throw new RestWorkerNotFoundException(e.getMessage());
        }
        return ResponseEntity.ok().body(workApplicationResponseList);
    }

    @PostMapping("/save")
    public ResponseEntity<?>saveWorkApplication(@RequestBody WorkApplicationSaveRequest workApplicationSaveRequest){
        WorkApplication workApplicationSave;
        try {
            workApplicationSave = WorkApplication.builder()
                    .worker(userService.getById(workApplicationSaveRequest.getWorkerId()))
                    .work(workService.getWorkById(workApplicationSaveRequest.getWorkId()))
                    .build();
            workApplicationSave = workApplicationService.saveWorkApplication(workApplicationSave);
        } catch (UserNotFound e) {
            throw new RestWorkerNotFoundException(e.getMessage());
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found",workApplicationSaveRequest.getWorkId()));
        } catch (WorkApplicationAlreadySend e) {
            throw new RestWorkApplicationAlreadySendException(e.getMessage());
        }
        WorkApplicationResponse workApplicationSaveResponse = WorkApplicationDtoUtil.getWorkApplicationRepsonse(workApplicationSave);
        return ResponseEntity.ok().body(workApplicationSaveResponse);
    }

    @PreAuthorize("hasAnyAuthority('HIRER','MODERATOR','ADMIN')")
    @PutMapping("/approve/{id_workapp}")
    public ResponseEntity<?>approveWorkApplicationById(@PathVariable("id_workapp")Long id_workapp){
        WorkApplication workApplicationApproved;
        try {
            workApplicationApproved = workApplicationService.approvedWorkApplication(id_workapp);
        } catch (WorkApplicationNotFound e) {
            throw new RestWorkApplicationNotFoundException(e.getMessage());
        } catch (WorkAlreadyAdded e) {
            throw new RestWorkAlreadyAddedException(e.getMessage());
        }
        WorkApplicationResponse workApplicationApprovedResponse = WorkApplicationDtoUtil.getWorkApplicationRepsonse(workApplicationApproved);
        return ResponseEntity.ok().body(workApplicationApprovedResponse);
    }
    @PreAuthorize("hasAnyAuthority('HIRER','MODERATOR','ADMIN')")
    @PutMapping("/reject/{id_workapp}")
    public ResponseEntity<?>rejectWorkApplicationById(@PathVariable("id_workapp")Long id_workapp){
        WorkApplication workApplicationReject;
        try {
           workApplicationReject = workApplicationService.rejectedWorkApplication(id_workapp);
        } catch (WorkApplicationNotFound e) {
            throw new RestWorkApplicationNotFoundException(e.getMessage());
        }
        WorkApplicationResponse workApplicationRejectResponse = WorkApplicationDtoUtil.getWorkApplicationRepsonse(workApplicationReject);
        return ResponseEntity.ok().body(workApplicationRejectResponse);
    }

}
