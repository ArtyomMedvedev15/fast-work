package io.project.fastwork.controller;

import io.project.fastwork.services.api.WorkServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/work")
@RequiredArgsConstructor
@Slf4j
public class WorkController {
    private final WorkServiceApi workService;


}
