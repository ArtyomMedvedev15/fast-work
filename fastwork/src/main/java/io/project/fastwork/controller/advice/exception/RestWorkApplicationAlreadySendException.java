package io.project.fastwork.controller.advice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RestWorkApplicationAlreadySendException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public RestWorkApplicationAlreadySendException() {
        super();
    }

    public RestWorkApplicationAlreadySendException(String message) {
        super(message);
    }
}
