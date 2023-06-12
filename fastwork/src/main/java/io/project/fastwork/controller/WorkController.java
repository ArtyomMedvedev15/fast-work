package io.project.fastwork.controller;

import io.project.fastwork.controller.advice.exception.RestTypeWorkNotFoundException;
import io.project.fastwork.controller.advice.exception.RestWorkAlreadyExistsException;
import io.project.fastwork.controller.advice.exception.RestWorkInvalidDataValuesException;
import io.project.fastwork.controller.advice.exception.RestWorkNotFoundException;
import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Work;
import io.project.fastwork.dto.request.WorkByNameRequest;
import io.project.fastwork.dto.request.WorkByTypeRequest;
import io.project.fastwork.dto.request.WorkSaveRequest;
import io.project.fastwork.dto.request.WorkUpdateRequest;
import io.project.fastwork.dto.response.WorkApplicationResponse;
import io.project.fastwork.dto.response.WorkResponse;
import io.project.fastwork.dto.util.WorkDtoUtil;
import io.project.fastwork.services.api.TypeWorkServiceApi;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.TypeWorkNotFound;
import io.project.fastwork.services.exception.WorkAlreadyExists;
import io.project.fastwork.services.exception.WorkInvalidDataValues;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static io.project.fastwork.dto.util.WorkDtoUtil.*;

@RestController
@RequestMapping("/api/v1/work")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Works", description = "Works request to API")
public class WorkController {
    private final WorkServiceApi workService;
    private final TypeWorkServiceApi typeWorkService;

    @Operation(
            summary = "Retrieve all works",
            description = "Get all work objects. The response is list of objects with work id, name, describe, count of person, price, type, hirer," +
                    "status, date of create.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get all work application of work.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<WorkResponse> workList = workService.findAllWork().stream().map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @Operation(
            summary = "Retrieve all works with status opened",
            description = "Get all work objects. The response is list of objects with work id, name, describe, count of person, price, type, hirer," +
                    "status, date of create.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get all work application of work.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @GetMapping("/openedwork")
    public ResponseEntity<?> getAllWorksOpen() {
        List<WorkResponse> workList = workService.findAllOpenedWork().stream().map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @Operation(
            summary = "Retrieve all works with status closed",
            description = "Get all work objects. The response is list of objects with work id, name, describe, count of person, price, type, hirer," +
                    "status, date of create.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get all work application of work.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @GetMapping("/closedwork")
    public ResponseEntity<?> getAllClosedWork() {
        List<WorkResponse> workList = workService.findAllClosedWork().stream().map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @Operation(
            summary = "Retrieve all works with status expectation",
            description = "Get all work objects. The response is list of objects with work id, name, describe, count of person, price, type, hirer," +
                    "status, date of create.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get all work application of work.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @GetMapping("/expectationwork")
    public ResponseEntity<?> getAllExpectationWork() {
        List<WorkResponse> workList = workService.findAllExceptionWork().stream().map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @Operation(
            summary = "Retrieve work by work id",
            description = "Get work object by work id. The response is object with work id, name, describe, count of person, price, type, hirer," +
                    "status, date of create.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get work by work id.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Work with work id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id_work}")
    public ResponseEntity<?> getWorkById(@Parameter(name = "id work", required = true,
            description = "Id for get work")
                                         @PathVariable("id_work") Long id_work) {
        Work work_by_id;
        try {
            work_by_id = workService.getWorkById(id_work);
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found", id_work));
        }
        WorkResponse workResponseById = getWorkResponse(work_by_id);
        return ResponseEntity.ok().body(workResponseById);
    }

    @Operation(
            summary = "Retrieve work by work name",
            description = "Get work objects by work name. The response is list of object with work id, name, describe, count of person, price, type, hirer," +
                    "status, date of create.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get work by work name.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PostMapping("/findbyname")
    public ResponseEntity<?> getAllWorksByName(@Parameter(name = "Request find work", required = true,
            description = "Object for find work by name")
                                               @RequestBody WorkByNameRequest workByNameRequest) {
        List<WorkResponse> workList = workService.findWorkByName(workByNameRequest.getWorkname()).stream()
                .map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @Operation(
            summary = "Retrieve work by work type",
            description = "Get work objects by work type. The response is list of object with work id, name, describe, count of person, price, type, hirer," +
                    "status, date of create.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get work by work type.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Type of work with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PostMapping("/findbytype")
    public ResponseEntity<?> getAllWorkByType(@Parameter(name = "Request find work", required = true,
            description = "Object for find work by type of work")
                                              @RequestBody WorkByTypeRequest workByTypeRequest) {
        TypeWork typeWorkById;
        try {
            typeWorkById = typeWorkService.getTypeWorkById(workByTypeRequest.getId_type());
        } catch (TypeWorkNotFound e) {
            throw new RestTypeWorkNotFoundException(String.format("Type work with id %s not found", workByTypeRequest.getId_type()));
        }
        List<WorkResponse> workList = workService.findWorkByTypeWork(typeWorkById).stream()
                .map(WorkDtoUtil::getWorkResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(workList);
    }

    @Operation(
            summary = "Save work",
            description = "Allows you to save new work")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully save new work.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Invalid parameter work.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('HIRER','ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<?> saveWork(@Parameter(name = "Request object for save work", required = true,
            description = "Object for save new work")
                                      @RequestBody WorkSaveRequest workSaveRequest) {
        Work work_save = getWorkFromSaveRequest(workSaveRequest);
        try {
            work_save = workService.saveWork(work_save);
        } catch (WorkAlreadyExists e) {
            throw new RestWorkAlreadyExistsException(String.format("Work with name %s already exists!", workSaveRequest.getWorkName()));
        } catch (WorkInvalidDataValues e) {
            throw new RestWorkInvalidDataValuesException("Work data isn't correct, try yet!");
        }
        WorkResponse workResponse = getWorkResponse(work_save);
        return ResponseEntity.ok().body(workResponse);
    }

    @Operation(
            summary = "Update work",
            description = "Allows you to update work")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully update work.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid parameter work.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Work with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('HIRER','ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateWork(@Parameter(name = "Request object for update work", required = true,
            description = "Object for update work")
                                        @RequestBody WorkUpdateRequest workUpdateRequest) {
        Work work_update = getWorkFromUpdateRequest(workUpdateRequest);
        try {
            Work work_old = workService.getWorkById(workUpdateRequest.getWorkId());
            work_update.setWorkStatus(work_old.getWorkStatus());
            work_update.setWorkDateCreate(work_old.getWorkDateCreate());
            work_update = workService.updateWork(work_update);
        } catch (WorkInvalidDataValues e) {
            throw new RestWorkInvalidDataValuesException("Work data isn't correct, try yet!");
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found", workUpdateRequest.getWorkId()));
        } catch (WorkAlreadyExists workAlreadyExists) {
            throw new RestWorkAlreadyExistsException(String.format("Work with name %s already exists!", workUpdateRequest.getWorkName()));
        }
        WorkResponse workResponse = getWorkResponse(work_update);
        return ResponseEntity.ok().body(workResponse);
    }

    @Operation(
            summary = "Close work",
            description = "Allows you to close work")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully close work.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Work with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('HIRER','ADMIN','MODERATOR')")
    @PostMapping("/closework/{id_work}")
    public ResponseEntity<?> closeWork(@Parameter(name = "id work", required = true,
            description = "Id for get work")
                                       @PathVariable("id_work") Long id_work) {
        Work work_closed;
        try {
            work_closed = workService.closeWork(workService.getWorkById(id_work));
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found", id_work));
        }
        WorkResponse workResponse = getWorkResponse(work_closed);
        return ResponseEntity.ok().body(workResponse);
    }

    @Operation(
            summary = "Expectation work",
            description = "Allows you to expectation work")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully expectation work.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Work with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PostMapping("/expectationwork/{id_work}")
    public ResponseEntity<?> exceptionWork(@Parameter(name = "id work", required = true,
            description = "Id for get work")
                                           @PathVariable("id_work") Long id_work) {
        Work work_exception;
        try {
            work_exception = workService.exceptionWork(workService.getWorkById(id_work));
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found", id_work));
        }
        WorkResponse workResponse = getWorkResponse(work_exception);
        return ResponseEntity.ok().body(workResponse);
    }

    @Operation(
            summary = "Open work",
            description = "Allows you to open work")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully open work.", content = {@Content(schema = @Schema(implementation = WorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Work with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PostMapping("/openwork/{id_work}")
    public ResponseEntity<?> openWork(@Parameter(name = "id work", required = true,
            description = "Id for get work")
                                      @PathVariable("id_work") Long id_work) {
        Work work_open;
        try {
            work_open = workService.openWork(workService.getWorkById(id_work));
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found", id_work));
        }
        WorkResponse workResponse = getWorkResponse(work_open);
        return ResponseEntity.ok().body(workResponse);
    }


}
