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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Worker", description = "Worker request to API")
public class WorkerController {

    private final UserServiceApi userService;
    private final WorkServiceApi workService;

    @Operation(
            summary = "Save work",
            description = "Allows you to save new work")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully add new work to worker.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Work already added to worker.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Work with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('WORKER','MODERATOR','ADMIN')")
    @PostMapping("/addwork")
    public ResponseEntity<?> addWorkWorker(@Parameter(name = "Request object for save work to worker", required = true,
            description = "Object for save new work to worker")
                                           @RequestBody AddWorkWorkerRequest addWorkWorkerRequest) {
        Work work;
        Users worker;
        try {
            worker = userService.getById(addWorkWorkerRequest.getWorkerId());
            work = workService.getWorkById(addWorkWorkerRequest.getWorkId());
            userService.addWorkToWorker(work, worker);
        } catch (UserNotFound e) {
            throw new RestWorkerNotFoundException(e.getMessage());
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found", addWorkWorkerRequest.getWorkId()));
        } catch (WorkAlreadyAdded e) {
            throw new RestWorkAlreadyAddedException(e.getMessage());
        }
        WorkResponse workResponseAdded = WorkDtoUtil.getWorkResponse(work);
        return ResponseEntity.ok().body(workResponseAdded);
    }

    @Operation(
            summary = "Remove work",
            description = "Allows you to remove work from worker")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully remove work from worker.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Work with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('WORKER','MODERATOR','ADMIN')")
    @PostMapping("/removework")
    public ResponseEntity<?> RemoveWorkWorker(@Parameter(name = "Request object for remove work from worker", required = true,
            description = "Object for remove work from worker")
                                                  @RequestBody RemoveWorkWorkerRequest removeWorkWorkerRequest) {
        Work work;
        Users worker;
        try {
            worker = userService.getById(removeWorkWorkerRequest.getWorkerId());
            work = workService.getWorkById(removeWorkWorkerRequest.getWorkId());
            userService.removeWorkFromWorker(work, worker);
        } catch (UserNotFound e) {
            throw new RestWorkerNotFoundException(e.getMessage());
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found", removeWorkWorkerRequest.getWorkId()));
        }

        WorkResponse workResponseRemoved = WorkDtoUtil.getWorkResponse(work);
        return ResponseEntity.ok().body(workResponseRemoved);
    }


}
