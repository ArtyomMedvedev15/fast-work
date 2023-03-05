package io.project.fastwork.controller;

import io.project.fastwork.controller.exception.RestTypeWorkAlreadyExistsException;
import io.project.fastwork.controller.exception.RestTypeWorkInvalidParameterException;
import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.dto.request.TypeWorkSaveRequest;
import io.project.fastwork.dto.response.TypeWorkResponse;
import io.project.fastwork.dto.util.TypeWorkDtoUtil;
import io.project.fastwork.services.api.TypeWorkServiceApi;
import io.project.fastwork.services.exception.TypeWorkAlreadyExistsException;
import io.project.fastwork.services.exception.TypeWorkInvalidParameterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.project.fastwork.dto.util.TypeWorkDtoUtil.getTypeWorkFromRequest;
import static io.project.fastwork.dto.util.TypeWorkDtoUtil.getTypeWorkResponse;

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

}
