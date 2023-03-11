package io.project.fastwork.controller.advice;

import io.project.fastwork.controller.exception.RestWorkApplicationAlreadySendException;
import io.project.fastwork.controller.exception.RestWorkApplicationNotFoundException;
import io.project.fastwork.controller.exception.RestWorkNotFoundException;
import io.project.fastwork.controller.exception.RestWorkerNotFoundException;
import io.project.fastwork.dto.response.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class WorkApplicationAdvice {
    @ExceptionHandler(value = RestWorkerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageResponse handleWorkerNotFoundException(RestWorkerNotFoundException ex, WebRequest request) {
        return ErrorMessageResponse.builder()
                .errro_statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .error_message(ex.getMessage())
                .error_description(request.getDescription(false)).build();
    }

    @ExceptionHandler(value = RestWorkApplicationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageResponse handleWorkApplicationNotFoundException(RestWorkApplicationNotFoundException ex, WebRequest request) {
        return ErrorMessageResponse.builder()
                .errro_statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .error_message(ex.getMessage())
                .error_description(request.getDescription(false)).build();
    }

    @ExceptionHandler(value = RestWorkApplicationAlreadySendException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageResponse handleWorkApplicationAlreadySendException(RestWorkApplicationAlreadySendException ex, WebRequest request) {
        return ErrorMessageResponse.builder()
                .errro_statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .error_message(ex.getMessage())
                .error_description(request.getDescription(false)).build();
    }
}
