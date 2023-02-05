package io.project.fastwork.services.util;

import io.project.fastwork.domains.Location;
import io.project.fastwork.services.exception.LocationWithInvalidArguments;

import java.util.regex.Pattern;

public class LocationValidator {

    public static boolean LocationValid(Location location) throws LocationWithInvalidArguments {
        boolean is_valid_location = false;
        double map_coordinate_x = Double.parseDouble(location.getLocationPoints().getX().toString());
        double map_coordinate_y = Double.parseDouble(location.getLocationPoints().getY().toString());
        if ((Pattern.matches("\\^[a-zA-Z]+$", location.getLocationCity()) &&
                Pattern.matches("\\^[a-zA-Z]+$", location.getLocationCountry()) &&
                Pattern.matches("\\^[a-zA-Z]+$", location.getLocationRegion()) &&
                Pattern.matches("\\^[a-zA-Z]+$", location.getLocationStreet())) && (
                (map_coordinate_x >= 0 && map_coordinate_x <= 180) && (map_coordinate_y >= 0 && map_coordinate_y <= 90)
        )){
           return true;
        }else{
            throw new LocationWithInvalidArguments("Check string parametrs and location x and y, something was wrong");
        }
    }

}
