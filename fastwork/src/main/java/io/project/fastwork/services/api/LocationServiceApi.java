package io.project.fastwork.services.api;

import io.project.fastwork.domains.Location;
import io.project.fastwork.services.exception.LocationNotFoundException;
import io.project.fastwork.services.exception.LocationWithInvalidArgumentsException;

import java.util.List;


public interface LocationServiceApi {
    Location saveLocation(Location savedLocation) throws LocationWithInvalidArgumentsException;
    Location updateLocation(Location updatedLocation) throws LocationWithInvalidArgumentsException;
    Location deleteLocation(Location deletedLocation) throws LocationNotFoundException;
    Location getByWork(Long work_id) throws LocationNotFoundException;
    List<Location>findLocationByCity(String city);
    List<Location>findLocationByNearby(double x,double y) throws LocationWithInvalidArgumentsException;

}
