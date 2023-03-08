package io.project.fastwork.controller.exception;

import java.io.Serial;

public class RestLocationWithInvalidArgumentsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public RestLocationWithInvalidArgumentsException() {
        super();
    }

    public RestLocationWithInvalidArgumentsException(String message) {
        super(message);
    }
}

