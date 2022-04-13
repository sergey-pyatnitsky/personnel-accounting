package com.personnel_accounting.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Date;

@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchDataException.class)
    public ResponseEntity<ApiError> handleNoSuchDataException(Exception e, WebRequest request){
        ApiError apiError = ApiErrorBuilder.anApiError()
                .withTimestamp(new Date(System.currentTimeMillis()))
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withError(e.getMessage()).build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistingDataException.class)
    public ResponseEntity<ApiError> handleExistingDataException(Exception e, WebRequest request){
        ApiError apiError = ApiErrorBuilder.anApiError()
                .withTimestamp(new Date(System.currentTimeMillis()))
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withError(e.getMessage()).build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ActiveStatusDataException.class)
    public ResponseEntity<ApiError> handleActiveStatusDataException(Exception e, WebRequest request){
        ApiError apiError = ApiErrorBuilder.anApiError()
                .withTimestamp(new Date(System.currentTimeMillis()))
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withError(e.getMessage()).build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OperationExecutionException.class)
    public ResponseEntity<ApiError> handleOperationExecutionException(Exception e, WebRequest request){
        ApiError apiError = ApiErrorBuilder.anApiError()
                .withTimestamp(new Date(System.currentTimeMillis()))
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withError(e.getMessage()).build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectDataException.class)
    public ResponseEntity<ApiError> handleIncorrectDataException(Exception e){
        ApiError apiError = ApiErrorBuilder.anApiError()
                .withTimestamp(new Date(System.currentTimeMillis()))
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withError(e.getMessage()).build();
        return new ResponseEntity<>(apiError, HttpStatus.OK);
    }
}
