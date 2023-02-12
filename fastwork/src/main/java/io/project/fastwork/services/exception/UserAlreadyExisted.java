package io.project.fastwork.services.exception;

public class UserAlreadyExisted extends Exception{
    public UserAlreadyExisted() {
        super();
    }

    public UserAlreadyExisted(String message) {
        super(message);
    }
}
