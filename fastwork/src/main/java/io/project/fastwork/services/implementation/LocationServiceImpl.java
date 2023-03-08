package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.Location;
import io.project.fastwork.repositories.LocationRepository;
import io.project.fastwork.services.api.LocationServiceApi;
import io.project.fastwork.services.exception.LocationNotFoundException;
import io.project.fastwork.services.exception.LocationWithInvalidArgumentsException;
import io.project.fastwork.services.util.LocationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class LocationServiceImpl implements LocationServiceApi {
    private final LocationRepository locationRepository;

    @Override
    public Location saveLocation(Location savedLocation) throws LocationWithInvalidArgumentsException {
        Location check_exists_location = locationRepository.getByWorkId(savedLocation.getLocationWork().getId());
        if(LocationValidator.LocationValidDataValues(savedLocation) && check_exists_location==null){
            log.info("Save new location for work with id {} in {}",savedLocation.getLocationWork().getId(),new Date());
            savedLocation.setLocationDateCreate(Timestamp.valueOf(LocalDateTime.now()));
            return locationRepository.save(savedLocation);
        }
        log.error("Invalid data for location, or try to save second location for work, throw exception with message {}",new LocationWithInvalidArgumentsException().getMessage());
        throw new LocationWithInvalidArgumentsException("Invalid data for location, or try to save second location for work, throw exception");
    }

    @Override
    public Location updateLocation(Location updatedLocation) throws LocationWithInvalidArgumentsException {
        if(LocationValidator.LocationValidDataValues(updatedLocation)){
            Location locationOld = locationRepository.getReferenceById(updatedLocation.getId());
            updatedLocation.setLocationWork(locationOld.getLocationWork());
            updatedLocation.setLocationDateCreate(locationOld.getLocationDateCreate());
            log.info("Update location with id {} for work with id {} in {}",updatedLocation.getId(),locationOld.getLocationWork().getId(),new Date());
            return locationRepository.save(updatedLocation);
        }
        log.error("Invalid data for location, throw exception with message {} in {}",new LocationWithInvalidArgumentsException().getMessage(), new Date());
        throw new LocationWithInvalidArgumentsException();
    }

    @Override
    public Location deleteLocation(Location deletedLocation) throws LocationNotFoundException {
        Location location_deleted_check=locationRepository.findById(deletedLocation.getId()).orElse(null);
        if(location_deleted_check!=null){
            log.warn("Delete location with id {} for work with id {} in {}",deletedLocation.getId(),deletedLocation.getLocationWork().getId(),new Date());
            locationRepository.delete(deletedLocation);
            return deletedLocation;
        }
        log.error("Location with id {} not found,throw exception in {}",deletedLocation.getId(),new Date());
        throw new LocationNotFoundException(String.format("Location with id - %s not found",deletedLocation.getId()));
    }

    @Override
    public Location getByWork(Long work_id) throws LocationNotFoundException {
        Location location_get_by_work_id=locationRepository.getByWorkId(work_id);
        if(location_get_by_work_id!=null){
            log.info("Get location by work id {} in {}",work_id,new Date());
            return location_get_by_work_id;
        }
        log.error("Location with work id {} not found,throw exception in {}",work_id,new Date());
        throw new LocationNotFoundException(String.format("Location with work id - %s not found",work_id));
     }

    @Override
    public List<Location> findLocationByCity(String city) {
        if(!city.isEmpty()){
            log.info("Get all location with city {} in {}",city,new Date());
            return locationRepository.findLocationByCity(city);
        }else{
            log.warn("Cannot get all location, city is empty in {}",new Date());
            return Collections.emptyList();
        }
    }
    @Override
    public List<Location> findLocationByNearby(double x, double y) throws LocationWithInvalidArgumentsException {
        if((x>=0 && x<=180) && (y>=0 && y <=90)){
            log.info("Gel all location by coordinates x - {} and y - {} in {}",x,y,new Date());
            return locationRepository.findLocationByNearby(x,y);
        }
        log.error("Cannot get location by coordinates, not correct value in {}",new Date());
        throw new LocationWithInvalidArgumentsException("Map сoordinates isn't correct!");
    }
}
