package io.project.fastwork.controller;

import io.project.fastwork.dto.response.TypeWorkResponse;
import io.project.fastwork.dto.util.TypeWorkDtoUtl;
import io.project.fastwork.services.api.TypeWorkServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/typework")
@RequiredArgsConstructor
@Slf4j
public class TypeWorkController {

    private final TypeWorkServiceApi typeWorkService;

    @GetMapping("/all")
    public ResponseEntity<?>getAllTypeWork(){
        List<TypeWorkResponse>typeWorkResponseList = typeWorkService.findAll().stream().map(TypeWorkDtoUtl::getTypeWorkResponse).toList();
        return ResponseEntity.ok().body(typeWorkResponseList);
    }

}
