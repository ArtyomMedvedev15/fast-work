package io.project.fastwork.controller;

import io.project.fastwork.controller.advice.exception.RestWorkAlreadyAddedException;
import io.project.fastwork.controller.advice.exception.RestWorkNotFoundException;
import io.project.fastwork.controller.advice.exception.RestWorkerNotFoundException;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.dto.request.AddWorkWorkerRequest;
import io.project.fastwork.dto.request.RemoveWorkWorkerRequest;
import io.project.fastwork.dto.response.WorkResponse;
import io.project.fastwork.dto.util.WorkDtoUtil;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.UserNotFound;
import io.project.fastwork.services.exception.WorkAlreadyAdded;
import io.project.fastwork.services.exception.WorkNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/worker")
@RequiredArgsConstructor
@Slf4j
public class WorkerController {

    private final UserServiceApi userService;
    private final WorkServiceApi workService;

    @PreAuthorize("hasAnyAuthority('WORKER','MODERATOR','ADMIN')")
    @PostMapping("/addwork")
    public ResponseEntity<?>addWorkWorker(@RequestBody AddWorkWorkerRequest addWorkWorkerRequest){
        Work work;
        Users worker;
        try {
            worker = userService.getById(addWorkWorkerRequest.getWorkerId());
            work = workService.getWorkById(addWorkWorkerRequest.getWorkId());
            userService.addWorkToWorker(work,worker);
        } catch (UserNotFound e) {
            throw new RestWorkerNotFoundException(e.getMessage());
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found",addWorkWorkerRequest.getWorkId()));
        } catch (WorkAlreadyAdded e) {
            throw new RestWorkAlreadyAddedException(e.getMessage());
        }
        WorkResponse workResponseAdded = WorkDtoUtil.getWorkResponse(work);
        return ResponseEntity.ok().body(workResponseAdded);
    }
    @PreAuthorize("hasAnyAuthority('WORKER','MODERATOR','ADMIN')")
    @PostMapping("/removework")
    public ResponseEntity<?>RemoveWorkWorker(@RequestBody RemoveWorkWorkerRequest removeWorkWorkerRequest){
        Work work;
        Users worker;
        try {
            worker = userService.getById(removeWorkWorkerRequest.getWorkerId());
            work = workService.getWorkById(removeWorkWorkerRequest.getWorkId());
            userService.removeWorkFromWorker(work,worker);
        } catch (UserNotFound e) {
            throw new RestWorkerNotFoundException(e.getMessage());
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found",removeWorkWorkerRequest.getWorkId()));
        }

        WorkResponse workResponseRemoved= WorkDtoUtil.getWorkResponse(work);
        return ResponseEntity.ok().body(workResponseRemoved);
    }


}
