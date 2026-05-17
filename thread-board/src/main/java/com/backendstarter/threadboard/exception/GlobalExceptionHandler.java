package com.backendstarter.threadboard.exception;

import com.backendstarter.threadboard.model.error.ClientErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<ClientErrorResponse> handleClientException(ClientErrorException e) {
        return new ResponseEntity<>(new ClientErrorResponse(e.getStatus(), e.getMessage()),
            e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ClientErrorResponse> handleClientException(
        MethodArgumentNotValidException e) {

        var errorMessage = e.getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .toList()
            .toString();

        return new ResponseEntity<>(
            new ClientErrorResponse(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ClientErrorResponse> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(
            new ClientErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ClientErrorResponse> handleException(Exception e) {
        return new ResponseEntity<>(
            new ClientErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
