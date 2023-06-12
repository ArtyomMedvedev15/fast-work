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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workapplication")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Work aplications for works", description = "Work Applications request to API")
public class WorkApplicationController {
    private final WorkApplicationServiceApi workApplicationService;
    private final WorkServiceApi workService;
    private final UserServiceApi userService;

    @Operation(
            summary = "Retrieve all work applications",
            description = "Get all work applications objects. The response is list of objects with worker, work, date of application, status of work application.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get all work application of work.", content = {@Content(schema = @Schema(implementation = WorkApplicationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @GetMapping("/all")
    public ResponseEntity<?> getAllWorkApplication() {
        List<WorkApplicationResponse> workApplicationResponses = workApplicationService.findAllWorkApplication().stream()
                .map(WorkApplicationDtoUtil::getWorkApplicationRepsonse).toList();
        return ResponseEntity.ok().body(workApplicationResponses);
    }

    @Operation(
            summary = "Retrieve all work applications by work id",
            description = "Get all work applications objects by work id. The response is list of objects with worker, work, date of application, status of work application.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get all work application of work by work id.", content = {@Content(schema = @Schema(implementation = WorkApplicationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Work with work id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @GetMapping("/findbywork/{id_work}")
    public ResponseEntity<?> getAllWorkApplicationByWorkId(@Parameter(name = "id work", required = true,
            description = "Id for get all work application of work") @PathVariable("id_work") Long id_work) {
        List<WorkApplicationResponse> workApplicationResponsesByWorkId;
        try {
            workApplicationResponsesByWorkId = workApplicationService.findByWorkId(id_work).stream()
                    .map(WorkApplicationDtoUtil::getWorkApplicationRepsonse).toList();
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found", id_work));
        }
        return ResponseEntity.ok().body(workApplicationResponsesByWorkId);
    }

    @Operation(
            summary = "Retrieve all work applications by worker id",
            description = "Get all work applications objects by worker id. The response is list of objects with worker, work, date of application, status of work application.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get all work application of work by worker id.", content = {@Content(schema = @Schema(implementation = WorkApplicationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Worker with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @GetMapping("/findbyworker/{id_worker}")
    public ResponseEntity<?> getAllWorkApplicationByWorkerId(@Parameter(name = "id worker", required = true,
            description = "Id for get all work application of work by worker id") @PathVariable("id_worker") Long id_worker) {
        List<WorkApplicationResponse> workApplicationResponseList;
        try {
            workApplicationResponseList = workApplicationService.findByWorkerid(id_worker).stream()
                    .map(WorkApplicationDtoUtil::getWorkApplicationRepsonse).toList();
        } catch (WorkerNotFound | UserNotFound e) {
            throw new RestWorkerNotFoundException(e.getMessage());
        }
        return ResponseEntity.ok().body(workApplicationResponseList);
    }

    @Operation(
            summary = "Save work application",
            description = "Allows you to save new work application of work")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully save new work application of work.", content = {@Content(schema = @Schema(implementation = WorkApplicationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Worker with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PostMapping("/save")
    public ResponseEntity<?> saveWorkApplication(@Parameter(name = "Request object for save work application", required = true,
            description = "Object for save new work application")
                                                 @RequestBody WorkApplicationSaveRequest workApplicationSaveRequest) {
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
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found", workApplicationSaveRequest.getWorkId()));
        } catch (WorkApplicationAlreadySend e) {
            throw new RestWorkApplicationAlreadySendException(e.getMessage());
        }
        WorkApplicationResponse workApplicationSaveResponse = WorkApplicationDtoUtil.getWorkApplicationRepsonse(workApplicationSave);
        return ResponseEntity.ok().body(workApplicationSaveResponse);
    }

    @Operation(
            summary = "Approve work application",
            description = "Allows you to approve work application of work")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully approve work application of work.", content = {@Content(schema = @Schema(implementation = WorkApplicationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Work application already approved.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Work application with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('HIRER','MODERATOR','ADMIN')")
    @PutMapping("/approve/{id_workapp}")
    public ResponseEntity<?> approveWorkApplicationById(@Parameter(name = "Id work application", required = true,
            description = "Id work application for approve")
                                                        @PathVariable("id_workapp") Long id_workapp) {
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

    @Operation(
            summary = "Reject work application",
            description = "Allows you to reject work application of work")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully approve work application of work.", content = {@Content(schema = @Schema(implementation = WorkApplicationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Work application with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('HIRER','MODERATOR','ADMIN')")
    @PutMapping("/reject/{id_workapp}")
    public ResponseEntity<?> rejectWorkApplicationById(@Parameter(name = "Id work application", required = true,
            description = "Id work application for approve")
                                                       @PathVariable("id_workapp") Long id_workapp) {
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
