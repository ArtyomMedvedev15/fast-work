package io.project.fastwork.controller;

import io.project.fastwork.domains.Work;
import io.project.fastwork.services.api.WorkServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


}
