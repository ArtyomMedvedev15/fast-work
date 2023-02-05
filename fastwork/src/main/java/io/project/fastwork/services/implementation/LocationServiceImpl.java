package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.Location;
import io.project.fastwork.domains.Work;
import io.project.fastwork.repositories.LocationRepository;
import io.project.fastwork.services.LocationServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationServiceApi {

    private final LocationRepository locationRepository;

    @Override
    public Location saveLocation(Location savedLocation) {
        return null;
    }

    @Override
    public Location updateLocation(Location updatedLocation) {
        return null;
    }

    @Override
    public Location deleteLocation(Location deletedLocation) {
        return null;
    }

    @Override
    public Location getByWork(Work work) {
        return null;
    }

    @Override
    public List<Location> findLocationByCity(String city) {
        return null;
    }

    @Override
    public List<Location> findLocationByNearby(double x, double y) {
        return null;
    }
}
