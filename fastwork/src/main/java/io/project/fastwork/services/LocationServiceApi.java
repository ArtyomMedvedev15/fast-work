package io.project.fastwork.services;

import io.project.fastwork.domains.Location;
import io.project.fastwork.domains.Work;
import io.project.fastwork.services.exception.LocationNotFound;
import io.project.fastwork.services.exception.LocationWithInvalidArguments;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationServiceApi {
    Location saveLocation(Location savedLocation) throws LocationWithInvalidArguments;
    Location updateLocation(Location updatedLocation) throws LocationWithInvalidArguments;
    Location deleteLocation(Location deletedLocation) throws LocationNotFound;
    Location getByWork(Long work_id) throws LocationNotFound;
    List<Location>findLocationByCity(String city);
    List<Location>findLocationByNearby(double x,double y) throws LocationWithInvalidArguments;

}
