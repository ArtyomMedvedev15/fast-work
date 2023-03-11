package io.project.fastwork.controller.advice;

import io.project.fastwork.controller.exception.RestWorkAlreadyAddedException;
import io.project.fastwork.controller.exception.RestWorkAlreadyExistsException;
import io.project.fastwork.controller.exception.RestWorkInvalidDataValuesException;
import io.project.fastwork.controller.exception.RestWorkNotFoundException;
import io.project.fastwork.dto.response.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class WorkControllerAdvice {
    @ExceptionHandler(value = RestWorkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageResponse handleWorkNotFoundException(RestWorkNotFoundException ex, WebRequest request) {
        return ErrorMessageResponse.builder()
                .errro_statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .error_message(ex.getMessage())
                .error_description(request.getDescription(false)).build();
    }

    @ExceptionHandler(value = RestWorkAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageResponse handleWorkAlreadyExistsException(RestWorkAlreadyExistsException ex, WebRequest request) {
        return ErrorMessageResponse.builder()
                .errro_statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .error_message(ex.getMessage())
                .error_description(request.getDescription(false)).build();
    }

    @ExceptionHandler(value = RestWorkInvalidDataValuesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageResponse handleWorkInvalidDataValuesException(RestWorkInvalidDataValuesException ex, WebRequest request) {
        return ErrorMessageResponse.builder()
                .errro_statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .error_message(ex.getMessage())
                .error_description(request.getDescription(false)).build();
    }

    @ExceptionHandler(value = RestWorkAlreadyAddedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageResponse handleWorkAlreadyAddedException(RestWorkAlreadyAddedException ex, WebRequest request) {
        return ErrorMessageResponse.builder()
                .errro_statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .error_message(ex.getMessage())
                .error_description(request.getDescription(false)).build();
    }

}
