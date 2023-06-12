package io.project.fastwork.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.project.fastwork.controller.advice.exception.RestTypeWorkAlreadyExistsException;
import io.project.fastwork.controller.advice.exception.RestTypeWorkInvalidParameterException;
import io.project.fastwork.controller.advice.exception.RestTypeWorkNotFoundException;
import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.dto.request.TypeWorkSaveRequest;
import io.project.fastwork.dto.request.TypeWorkUpdateRequest;
import io.project.fastwork.dto.response.TypeWorkResponse;
import io.project.fastwork.dto.util.TypeWorkDtoUtil;
import io.project.fastwork.services.api.TypeWorkServiceApi;
import io.project.fastwork.services.exception.TypeWorkAlreadyExistsException;
import io.project.fastwork.services.exception.TypeWorkInvalidParameterException;
import io.project.fastwork.services.exception.TypeWorkNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.project.fastwork.dto.util.TypeWorkDtoUtil.*;

@RestController
@RequestMapping("/api/v1/typework")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Type of works", description = "Type works request to API")
public class TypeWorkController {

    private final TypeWorkServiceApi typeWorkService;

    @Operation(
            summary = "Retrieve all Type works",
            description = "Get a Type of works objects. The response is object with id, name, description.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get all type of works.", content = {@Content(schema = @Schema(implementation = TypeWorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<?> getAllTypeWork() {
        List<TypeWorkResponse> typeWorkResponseList = typeWorkService.findAll().stream().map(TypeWorkDtoUtil::getTypeWorkResponse).toList();
        return ResponseEntity.ok().body(typeWorkResponseList);
    }

    @Operation(
            summary = "Retrieve type work by id",
            description = "Get a Type work object by id. The response is object with id, name, description.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get type of work by id.", content = {@Content(schema = @Schema(implementation = TypeWorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Type work with your id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id_type_work}")
    public ResponseEntity<?> getTypeWorkById(@Parameter(name = "id type work", required = true, description = "Id for get type work") @PathVariable("id_type_work") Long id_type_work) {
        TypeWork typeWorkById;
        try {
            typeWorkById = typeWorkService.getTypeWorkById(id_type_work);
        } catch (TypeWorkNotFound e) {
            throw new RestTypeWorkNotFoundException(String.format("Type work with id %s not found", id_type_work));
        }
        TypeWorkResponse typeWorkResponse = getTypeWorkResponse(typeWorkById);
        return ResponseEntity.ok().body(typeWorkResponse);
    }

    @Operation(
            summary = "Save new type work",
            description = "Allows you to save new type of work.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully save type of work.", content = {@Content(schema = @Schema(implementation = TypeWorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Attempt to save an object with an already existing name.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PostMapping("/save")
    public ResponseEntity<?> saveTypeWork(@Parameter(name = "Type work request object", required = true, description = "Object in json format for save type of work") @RequestBody TypeWorkSaveRequest typeWorkSaveRequest) {
        TypeWork typeWorkSave = getTypeWorkFromRequest(typeWorkSaveRequest);
        try {
            typeWorkSave = typeWorkService.saveTypeWork(typeWorkSave);
        } catch (TypeWorkInvalidParameterException e) {
            throw new RestTypeWorkInvalidParameterException("Type work data isn't correct!");
        } catch (TypeWorkAlreadyExistsException e) {
            throw new RestTypeWorkAlreadyExistsException(String.format("Type work with name - %s already exists!", typeWorkSaveRequest.getTypeWorkName()));
        }
        TypeWorkResponse typeWorkResponse = getTypeWorkResponse(typeWorkSave);
        return ResponseEntity.ok().body(typeWorkResponse);
    }

    @Operation(
            summary = "Update type work",
            description = "Allows you to update type of work.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully update type of work.", content = {@Content(schema = @Schema(implementation = TypeWorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Attempt to save an object with an already existing name.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PutMapping("/update")
    public ResponseEntity<?> updateTypeWork(@Parameter(name = "Type work request object", required = true, description = "Object in json format for update type of work") @RequestBody TypeWorkUpdateRequest typeWorkUpdateRequest) {
        TypeWork typeWorkUpdate = getTypeWorkFromUpdateRequest(typeWorkUpdateRequest);
        try {
            typeWorkUpdate = typeWorkService.updateTypeWork(typeWorkUpdate);
        } catch (TypeWorkAlreadyExistsException e) {
            throw new RestTypeWorkAlreadyExistsException(String.format("Type work with name - %s already exists!", typeWorkUpdate.getTypeWorkName()));
        } catch (TypeWorkInvalidParameterException e) {
            throw new RestTypeWorkInvalidParameterException("Type work data isn't correct!");
        }
        TypeWorkResponse typeWorkResponseUpdate = getTypeWorkResponse(typeWorkUpdate);
        return ResponseEntity.ok().body(typeWorkResponseUpdate);
    }

    @Operation(
            summary = "Delete type work",
            description = "Allows you to delete type of work.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully delete type of work.", content = {@Content(schema = @Schema(implementation = TypeWorkResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Type work with your id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @DeleteMapping("/delete/{id_type_work}")
    public ResponseEntity<?> deleteTypeWork(@Parameter(name = "id type work", required = true, description = "Id for get type work") @PathVariable("id_type_work") Long id_type_work) {
        TypeWork typeWorkDelete;
        try {
            typeWorkDelete = typeWorkService.getTypeWorkById(id_type_work);
            typeWorkDelete = typeWorkService.deleteTypeWork(typeWorkDelete);
        } catch (TypeWorkNotFound e) {
            throw new RestTypeWorkNotFoundException(String.format("Type work with id %s not found", id_type_work));
        }
        TypeWorkResponse typeWorkResponseDelete = getTypeWorkResponse(typeWorkDelete);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(typeWorkResponseDelete);
    }

}
