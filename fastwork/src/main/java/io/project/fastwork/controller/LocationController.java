package io.project.fastwork.controller;

import io.project.fastwork.controller.advice.exception.RestLocationNotFoundException;
import io.project.fastwork.controller.advice.exception.RestLocationWithInvalidArgumentsException;
import io.project.fastwork.controller.advice.exception.RestWorkNotFoundException;
import io.project.fastwork.domains.Location;
import io.project.fastwork.dto.request.LocationByCityRequest;
import io.project.fastwork.dto.request.LocationByNearbyRequest;
import io.project.fastwork.dto.request.LocationSaveRequest;
import io.project.fastwork.dto.request.LocationUpdateRequest;
import io.project.fastwork.dto.response.LocationResponse;
import io.project.fastwork.dto.util.LocationDtoUtil;
import io.project.fastwork.services.api.LocationServiceApi;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.LocationNotFoundException;
import io.project.fastwork.services.exception.LocationWithInvalidArgumentsException;
import io.project.fastwork.services.exception.WorkNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static io.project.fastwork.dto.util.LocationDtoUtil.*;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
@Slf4j
public class LocationController {
    private final LocationServiceApi locationService;
    private final WorkServiceApi workService;
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
        List<LocationResponse>locationsByCity = locationService.findLocationByCity(locationByCityRequest.getLocationCity()).stream().map(LocationDtoUtil::getLocationResponseFromDomain).toList();
        return ResponseEntity.ok().body(locationsByCity);
    }

    @PostMapping("/findbynearby")
    public ResponseEntity<?>findLocationByNearby(@RequestBody LocationByNearbyRequest locationByNearbyRequest){
        List<LocationResponse>locationsByNearby;
        try {
            locationsByNearby = locationService.findLocationByNearby(locationByNearbyRequest.getLocationX(),locationByNearbyRequest.getLocationY()).stream()
                    .map(LocationDtoUtil::getLocationResponseFromDomain).collect(Collectors.toList());
        } catch (LocationWithInvalidArgumentsException e) {
            throw new RestLocationWithInvalidArgumentsException("Location coordinates isn't correct!");
        }
        return ResponseEntity.ok().body(locationsByNearby);
    }

    @PostMapping("/save")
    public ResponseEntity<?>saveLocation(@RequestBody LocationSaveRequest locationSaveRequest){
        Location locationSave;
        try {
            locationSave = getLocationFromSaveRequest(locationSaveRequest);
            locationSave.setLocationWork(workService.getWorkById(locationSaveRequest.getLocationWorkId()));
            locationSave = locationService.saveLocation(locationSave);
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found",locationSaveRequest.getLocationWorkId()));
        } catch (LocationWithInvalidArgumentsException e) {
            throw new RestLocationWithInvalidArgumentsException(e.getMessage());
        }
        LocationResponse locationResponse = LocationDtoUtil.getLocationResponseFromDomain(locationSave);
        return ResponseEntity.ok().body(locationResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<?>updateLocation(@RequestBody LocationUpdateRequest locationUpdateRequest){
        Location locationUpdate;
        try {
            locationUpdate = getLocationFromUpdateRequest(locationUpdateRequest);
            locationUpdate = locationService.updateLocation(locationUpdate);
        } catch (LocationWithInvalidArgumentsException e) {
            throw new RestLocationWithInvalidArgumentsException(e.getMessage());
        }
        LocationResponse locationResponse = LocationDtoUtil.getLocationResponseFromDomain(locationUpdate);
        return ResponseEntity.ok().body(locationResponse);
    }

    @DeleteMapping("/delete/{location_id}")
    public ResponseEntity<?>deleteLocation(@PathVariable("location_id")Long location_id){
        Location deletedLocation;
        try {
            deletedLocation = locationService.deleteLocation(Location.builder().id(location_id).build());
        } catch (LocationNotFoundException e) {
            throw new RestLocationNotFoundException(String.format("Location with id %s not found!", location_id));
        }

        return ResponseEntity.noContent().build();
    }

}
