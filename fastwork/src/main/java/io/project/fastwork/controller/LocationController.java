package io.project.fastwork.controller;

import io.project.fastwork.controller.exception.RestLocationNotFoundException;
import io.project.fastwork.domains.Location;
import io.project.fastwork.dto.request.LocationByCityRequest;
import io.project.fastwork.dto.response.LocationResponse;
import io.project.fastwork.dto.util.LocationDtoUtil;
import io.project.fastwork.services.api.LocationServiceApi;
import io.project.fastwork.services.exception.LocationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static io.project.fastwork.dto.util.LocationDtoUtil.getLocationResponseFromDomain;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
@Slf4j
public class LocationController {
    private final LocationServiceApi locationService;
    @GetMapping("/work/{id_work}")
    public ResponseEntity<?> getLocationByWorkId(@PathVariable("id_work") Long work_id) {
        Location location_by_work;
        try {
            location_by_work = locationService.getByWork(work_id);
        } catch (LocationNotFoundException e) {
            throw new RestLocationNotFoundException(String.format("Location with work id %s not found!", work_id));
        }
        LocationResponse location_by_id_response = getLocationResponseFromDomain(location_by_work);
        return ResponseEntity.ok().body(location_by_id_response);
    }

    @PostMapping("/findbycity")
    public ResponseEntity<?>findLocationByCity(@RequestBody LocationByCityRequest locationByCityRequest){
        List<LocationResponse>locationsByCity = locationService.findLocationByCity(locationByCityRequest.getCity()).stream().map(LocationDtoUtil::getLocationResponseFromDomain).toList();
        return ResponseEntity.ok().body(locationsByCity);
    }

}
