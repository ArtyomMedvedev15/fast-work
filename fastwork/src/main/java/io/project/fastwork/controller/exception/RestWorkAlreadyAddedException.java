package io.project.fastwork.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RestWorkAlreadyAddedException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public RestWorkAlreadyAddedException() {
        super();
    }

    public RestWorkAlreadyAddedException(String message) {
        super(message);
    }
}

