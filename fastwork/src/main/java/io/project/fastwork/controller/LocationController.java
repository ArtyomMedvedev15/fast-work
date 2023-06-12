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
import io.project.fastwork.dto.response.TypeWorkResponse;
import io.project.fastwork.dto.util.LocationDtoUtil;
import io.project.fastwork.services.api.LocationServiceApi;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.LocationNotFoundException;
import io.project.fastwork.services.exception.LocationWithInvalidArgumentsException;
import io.project.fastwork.services.exception.WorkNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static io.project.fastwork.dto.util.LocationDtoUtil.*;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Location for works", description = "Locations request to API")
public class LocationController {
    private final LocationServiceApi locationService;
    private final WorkServiceApi workService;

    @Operation(
            summary = "Retrieve location by work id",
            description = "Get a location object. The response is object with country, city, region, street, lat and lag position on map, work id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get location of work.", content = {@Content(schema = @Schema(implementation = LocationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Location with work id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @GetMapping("/work/{id_work}")
    public ResponseEntity<?> getLocationByWorkId(@Parameter(name = "id work", required = true,
            description = "Id for get location of work") @PathVariable("id_work") Long work_id) {
        Location location_by_work;
        try {
            location_by_work = locationService.getByWork(work_id);
        } catch (LocationNotFoundException e) {
            throw new RestLocationNotFoundException(String.format("Location with work id %s not found!", work_id));
        }
        LocationResponse location_by_id_response = getLocationResponseFromDomain(location_by_work);
        return ResponseEntity.ok().body(location_by_id_response);
    }

    @Operation(
            summary = "Retrieve locations by city",
            description = "Get all locations objects. The response is list of objects with country, city, region, street, lat and lag position on map, work id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get all locations by city.", content = {@Content(schema = @Schema(implementation = LocationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PostMapping("/findbycity")
    public ResponseEntity<?> findLocationByCity(@Parameter(name = "Request object for find locations", required = true,
            description = "Name city for get all locations for works") @RequestBody LocationByCityRequest locationByCityRequest) {
        List<LocationResponse> locationsByCity = locationService.findLocationByCity(locationByCityRequest.getLocationCity()).stream().map(LocationDtoUtil::getLocationResponseFromDomain).toList();
        return ResponseEntity.ok().body(locationsByCity);
    }

    @Operation(
            summary = "Retrieve locations by nearby lat and lag",
            description = "Get all locations objects. The response is list of objects with country, city, region, street, lat and lag position on map, work id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully get all locations by nearby.", content = {@Content(schema = @Schema(implementation = LocationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Error with parameter lat and lag.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PostMapping("/findbynearby")
    public ResponseEntity<?> findLocationByNearby(@Parameter(name = "Request object for find locations", required = true,
            description = "Lat and lag for get all locations for works") @RequestBody LocationByNearbyRequest locationByNearbyRequest) {
        List<LocationResponse> locationsByNearby;
        try {
            locationsByNearby = locationService.findLocationByNearby(locationByNearbyRequest.getLocationX(), locationByNearbyRequest.getLocationY()).stream()
                    .map(LocationDtoUtil::getLocationResponseFromDomain).collect(Collectors.toList());
        } catch (LocationWithInvalidArgumentsException e) {
            throw new RestLocationWithInvalidArgumentsException("Location coordinates isn't correct!");
        }
        return ResponseEntity.ok().body(locationsByNearby);
    }

    @Operation(
            summary = "Save location for work",
            description = "Allows you to save new location for work")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully save location.", content = {@Content(schema = @Schema(implementation = LocationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Work with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Error with parameter lat and lag.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('HIRER','ADMIN','MODERATOR')")
    @PostMapping("/save")
    public ResponseEntity<?> saveLocation(@Parameter(name = "Request object for save location", required = true,
            description = "Save new location with country, city, region, street, lat and lag position on map")
                                          @RequestBody LocationSaveRequest locationSaveRequest) {
        Location locationSave;
        try {
            locationSave = getLocationFromSaveRequest(locationSaveRequest);
            locationSave.setLocationWork(workService.getWorkById(locationSaveRequest.getLocationWorkId()));
            locationSave = locationService.saveLocation(locationSave);
        } catch (WorkNotFound e) {
            throw new RestWorkNotFoundException(String.format("Work with id - %s not found", locationSaveRequest.getLocationWorkId()));
        } catch (LocationWithInvalidArgumentsException e) {
            throw new RestLocationWithInvalidArgumentsException(e.getMessage());
        }
        LocationResponse locationResponse = LocationDtoUtil.getLocationResponseFromDomain(locationSave);
        return ResponseEntity.ok().body(locationResponse);
    }

    @Operation(
            summary = "Update location for work",
            description = "Allows you to update location for work")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully update location.", content = {@Content(schema = @Schema(implementation = LocationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Error with parameter lat and lag.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('HIRER','ADMIN','MODERATOR')")
    @PutMapping("/update")
    public ResponseEntity<?> updateLocation(@Parameter(name = "Request object for update location", required = true,
            description = "Update location with country, city, region, street, lat and lag position on map")
                                            @RequestBody LocationUpdateRequest locationUpdateRequest) {
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

    @Operation(
            summary = "Delete location of work",
            description = "Allows you to delete location of work")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully delete location.", content = {@Content(schema = @Schema(implementation = LocationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "You need to get a token to pass it to the request header.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Work with id was not found.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PreAuthorize("hasAnyAuthority('HIRER','ADMIN','MODERATOR')")
    @DeleteMapping("/delete/{location_id}")
    public ResponseEntity<?> deleteLocation(@Parameter(name = "id work", required = true,
            description = "Id for get location of work") @PathVariable("location_id") Long location_id) {
        Location deletedLocation;
        try {
            deletedLocation = locationService.deleteLocation(Location.builder().id(location_id).build());
        } catch (LocationNotFoundException e) {
            throw new RestLocationNotFoundException(String.format("Location with id %s not found!", location_id));
        }

        return ResponseEntity.noContent().build();
    }

}
