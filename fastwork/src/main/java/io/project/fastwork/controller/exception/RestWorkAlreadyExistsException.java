package io.project.fastwork.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RestWorkAlreadyExistsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public RestWorkAlreadyExistsException() {
        super();
    }

    public RestWorkAlreadyExistsException(String message) {
        super(message);
    }
}
