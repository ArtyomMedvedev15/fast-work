package io.project.fastwork.controller.exception;

import java.io.Serial;

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
