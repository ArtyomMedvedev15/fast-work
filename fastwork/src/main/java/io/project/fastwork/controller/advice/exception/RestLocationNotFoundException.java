package io.project.fastwork.controller.advice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestLocationNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public RestLocationNotFoundException() {
        super();
    }

    public RestLocationNotFoundException(String message) {
        super(message);
    }
}
