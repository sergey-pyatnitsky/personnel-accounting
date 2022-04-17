package com.personnel_accounting.exception;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Date;

@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LogManager.getLogger("MainExceptionHandler logger");

    @ExceptionHandler({NoSuchDataException.class, ExistingDataException.class,
            ActiveStatusDataException.class, OperationExecutionException.class,
            IncorrectDataException.class})
    public ResponseEntity<ApiError> handleCustomException(Exception e) {
        ApiError apiError = ApiError.ApiErrorBuilder.anApiError()
                .withTimestamp(new Date(System.currentTimeMillis()))
                .withStatus(HttpStatus.OK.value())
                .withError(e.getMessage()).build();
        logger.error(e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = ApiError.ApiErrorBuilder.anApiError()
                .withTimestamp(new Date(System.currentTimeMillis()))
                .withStatus(HttpStatus.OK.value())
                .withError(e.getBindingResult().getFieldError().getDefaultMessage()).build();
        logger.error(e.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<>(apiError, HttpStatus.OK);
    }
}
