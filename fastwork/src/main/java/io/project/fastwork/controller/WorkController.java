package io.project.fastwork.controller;

import io.project.fastwork.controller.exception.RestTypeWorkNotFoundException;
import io.project.fastwork.controller.exception.RestWorkAlreadyExistsException;
import io.project.fastwork.controller.exception.RestWorkInvalidDataValuesException;
import io.project.fastwork.controller.exception.RestWorkNotFoundException;
import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.dto.request.WorkByNameRequest;
import io.project.fastwork.dto.request.WorkByTypeRequest;
import io.project.fastwork.dto.request.WorkSaveRequest;
import io.project.fastwork.dto.response.WorkResponse;
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

import static io.project.fastwork.dto.util.WorkDtoUtil.getWorkFromRequest;
import static io.project.fastwork.dto.util.WorkDtoUtil.getWorkResponse;

@RestController
@RequestMapping("/api/v1/work")
@RequiredArgsConstructor
@Slf4j
public class WorkController {
    private final WorkServiceApi workService;
    private final TypeWorkServiceApi typeWorkService;


    @GetMapping("/all")
    public ResponseEntity<?>getAllWorks(){
        List<Work>workList=workService.findAllOpenedWork();
        return ResponseEntity.ok().body(workList);
    }

    @GetMapping("/{id_work}")
    public ResponseEntity<?>getAllWorks(@PathVariable("id_work")Long id_work){
        Work work_by_id;
        try {
            work_by_id = workService.getWorkById(id_work);
        } catch (WorkNotFound e) {
            e.printStackTrace();
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found",id_work));
        }
        return ResponseEntity.ok().body(work_by_id);
    }

    @PostMapping("/findbyname")
    public ResponseEntity<?>getAllWorksByName(@RequestBody WorkByNameRequest workByNameRequest){
        List<Work>workList = workService.findWorkByName(workByNameRequest.getWorkname());
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
        List<Work>workList = workService.findWorkByTypeWork(typeWorkById);
        return ResponseEntity.ok().body(workList);
    }

    @PostMapping("/save")
    public ResponseEntity<?>saveWork(@RequestBody WorkSaveRequest workSaveRequest){
        Work work_save = getWorkFromRequest(workSaveRequest);
        try {
            workService.saveWork(work_save);
        } catch (WorkAlreadyExists e) {
            throw new RestWorkAlreadyExistsException(String.format("Work with name %s already exists!",workSaveRequest.getWorkName()));
        } catch (WorkInvalidDataValues e) {
            throw new RestWorkInvalidDataValuesException("Work data isn't correct, try yet!");
        }
        WorkResponse workResponse = getWorkResponse(workSaveRequest);
        return ResponseEntity.ok().body(workResponse);
    }


}
