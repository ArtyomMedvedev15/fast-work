package io.project.fastwork.controller.advice;

import io.project.fastwork.controller.advice.exception.RestLocationNotFoundException;
import io.project.fastwork.controller.advice.exception.RestLocationWithInvalidArgumentsException;
import io.project.fastwork.dto.response.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class LocationControllerAdvice {
    @ExceptionHandler(value = RestLocationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageResponse handleLocationNotFoundException(RestLocationNotFoundException ex, WebRequest request) {
        return ErrorMessageResponse.builder()
                .errro_statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .error_message(ex.getMessage())
                .error_description(request.getDescription(false)).build();
    }

    @ExceptionHandler(value = RestLocationWithInvalidArgumentsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageResponse handleLocationWithInvalidArgumentsException(RestLocationWithInvalidArgumentsException ex, WebRequest request) {
        return ErrorMessageResponse.builder()
                .errro_statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .error_message(ex.getMessage())
                .error_description(request.getDescription(false)).build();
    }
}
