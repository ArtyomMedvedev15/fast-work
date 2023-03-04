package io.project.fastwork.controller;

import io.project.fastwork.controller.exception.RestWorkNotFoundException;
import io.project.fastwork.domains.Work;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.WorkNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/work")
@RequiredArgsConstructor
@Slf4j
public class WorkController {
    private final WorkServiceApi workService;

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


}
