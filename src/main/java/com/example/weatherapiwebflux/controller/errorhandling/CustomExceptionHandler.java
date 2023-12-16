package com.example.weatherapiwebflux.controller.errorhandling;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.MissingRequestValueException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = {HandlerMethodValidationException.class})
    public ResponseEntity<ErrorMessage> handleValidationException(HandlerMethodValidationException ex) {
        List<String> errors = new ArrayList<>();
        for (ParameterValidationResult pvr:ex.getAllValidationResults()) {
            for (MessageSourceResolvable msr:pvr.getResolvableErrors()) {
                errors.add(pvr.getMethodParameter().getParameterName() + " " + msr.getDefaultMessage());
            }
        }
        ErrorMessage apiError = ErrorMessage.builder()
                                            .timestamp(LocalDateTime.now())
                                            .status(HttpStatus.BAD_REQUEST)
                                            .message(ex.getReason())
                                            .errors(errors)
                                            .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MissingRequestValueException.class})
    public ResponseEntity<ErrorMessage> handleMissingParam(MissingRequestValueException ex) {
        ErrorMessage apiError = ErrorMessage.builder()
                                            .timestamp(LocalDateTime.now())
                                            .status(HttpStatus.BAD_REQUEST)
                                            .message("Missing request parameter")
                                            .errors(List.of(ex.getReason()))
                                            .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
