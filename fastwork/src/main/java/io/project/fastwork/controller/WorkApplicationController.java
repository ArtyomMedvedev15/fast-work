package io.project.fastwork.controller;

import io.project.fastwork.dto.response.WorkApplicationResponse;
import io.project.fastwork.dto.util.WorkApplicationDtoUtil;
import io.project.fastwork.services.api.WorkApplicationServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/workapplication")
@RequiredArgsConstructor
@Slf4j
public class WorkApplicationController {
    private final WorkApplicationServiceApi workApplicationService;

    @GetMapping("/all")
    public ResponseEntity<?>getAllWorkApplication(){
        List<WorkApplicationResponse>workApplicationResponses= workApplicationService.findAllWorkApplication().stream().map(WorkApplicationDtoUtil::getWorkApplicationRepsonse).toList();
        return ResponseEntity.ok().body(workApplicationResponses);
    }
}
