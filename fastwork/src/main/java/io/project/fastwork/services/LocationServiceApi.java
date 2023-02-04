package io.project.fastwork.services;

import io.project.fastwork.domains.Location;
import io.project.fastwork.domains.Work;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationServiceApi {
    Location saveLocation(Location savedLocation);
    Location updateLocation(Location updatedLocation);
    Location deleteLocation(Location deletedLocation);
    Location getByWork(Work work);
    List<Location>findLocationByCity(String city);
    List<Location>findLocationByNearby(double x,double y);

}
