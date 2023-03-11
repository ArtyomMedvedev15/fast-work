package io.project.fastwork.controller.exception;

import java.io.Serial;

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
