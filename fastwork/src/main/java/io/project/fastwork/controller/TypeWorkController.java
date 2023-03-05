package io.project.fastwork.controller;

import io.project.fastwork.controller.exception.RestTypeWorkAlreadyExistsException;
import io.project.fastwork.controller.exception.RestTypeWorkInvalidParameterException;
import io.project.fastwork.controller.exception.RestTypeWorkNotFoundException;
import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.dto.request.TypeWorkSaveRequest;
import io.project.fastwork.dto.request.TypeWorkUpdateRequest;
import io.project.fastwork.dto.response.TypeWorkResponse;
import io.project.fastwork.dto.util.TypeWorkDtoUtil;
import io.project.fastwork.services.api.TypeWorkServiceApi;
import io.project.fastwork.services.exception.TypeWorkAlreadyExistsException;
import io.project.fastwork.services.exception.TypeWorkInvalidParameterException;
import io.project.fastwork.services.exception.TypeWorkNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.project.fastwork.dto.util.TypeWorkDtoUtil.*;

@RestController
@RequestMapping("/api/v1/typework")
@RequiredArgsConstructor
@Slf4j
public class TypeWorkController {

    private final TypeWorkServiceApi typeWorkService;

    @GetMapping("/all")
    public ResponseEntity<?>getAllTypeWork(){
        List<TypeWorkResponse>typeWorkResponseList = typeWorkService.findAll().stream().map(TypeWorkDtoUtil::getTypeWorkResponse).toList();
        return ResponseEntity.ok().body(typeWorkResponseList);
    }

    @GetMapping("/{id_type_work}")
    public ResponseEntity<?>getTypeWorkById(@PathVariable("id_type_work")Long id_type_work){
        TypeWork typeWorkById;
        try {
            typeWorkById = typeWorkService.getTypeWorkById(id_type_work);
        } catch (TypeWorkNotFound e) {
            throw new RestTypeWorkNotFoundException(String.format("Type work with id %s not found",id_type_work));
        }
        TypeWorkResponse typeWorkResponse = getTypeWorkResponse(typeWorkById);
        return ResponseEntity.ok().body(typeWorkResponse);
    }

    @PostMapping("/save")
    public ResponseEntity<?>saveTypeWork(@RequestBody TypeWorkSaveRequest typeWorkSaveRequest){
        TypeWork typeWorkSave = getTypeWorkFromRequest(typeWorkSaveRequest);
        try {
            typeWorkSave = typeWorkService.saveTypeWork(typeWorkSave);
        } catch (TypeWorkInvalidParameterException e) {
            throw new RestTypeWorkInvalidParameterException("Type work data isn't correct!");
        } catch (TypeWorkAlreadyExistsException e) {
            throw new RestTypeWorkAlreadyExistsException(String.format("Type work with name - %s already exists!",typeWorkSaveRequest.getTypeWorkName()));
        }
        TypeWorkResponse typeWorkResponse = getTypeWorkResponse(typeWorkSave);
        return ResponseEntity.ok().body(typeWorkResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<?>updateTypeWork(@RequestBody TypeWorkUpdateRequest typeWorkUpdateRequest){
        TypeWork typeWorkUpdate = getTypeWorkFromUpdateRequest(typeWorkUpdateRequest);
        try {
            typeWorkUpdate = typeWorkService.updateTypeWork(typeWorkUpdate);
        } catch (TypeWorkAlreadyExistsException e) {
            throw new RestTypeWorkInvalidParameterException("Type work data isn't correct!");
        } catch (TypeWorkInvalidParameterException e) {
            throw new RestTypeWorkAlreadyExistsException(String.format("Type work with name - %s already exists!",typeWorkUpdate.getTypeWorkName()));
        }
        TypeWorkResponse typeWorkResponseUpdate = getTypeWorkResponse(typeWorkUpdate);
        return ResponseEntity.ok().body(typeWorkResponseUpdate);
    }

}
