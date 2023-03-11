package io.project.fastwork.controller.advice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestWorkApplicationNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public RestWorkApplicationNotFoundException() {
        super();
    }

    public RestWorkApplicationNotFoundException(String message) {
        super(message);
    }
}

