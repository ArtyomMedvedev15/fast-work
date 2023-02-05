package io.project.fastwork.services.exception;

public class LocationWithInvalidArguments extends Exception{
    public LocationWithInvalidArguments() {
        super();
    }

    public LocationWithInvalidArguments(String message) {
        super(message);
    }
}
