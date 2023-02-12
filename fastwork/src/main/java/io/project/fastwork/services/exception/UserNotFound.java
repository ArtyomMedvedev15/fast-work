package io.project.fastwork.services.exception;

public class UserNotFound extends Exception{
    public UserNotFound() {
        super();
    }

    public UserNotFound(String message) {
        super(message);
    }
}
