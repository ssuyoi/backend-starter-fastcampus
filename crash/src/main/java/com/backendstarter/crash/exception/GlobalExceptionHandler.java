package com.backendstarter.crash.exception;

import com.backendstarter.crash.model.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleClientErrorException(ClientErrorException e) {
        return new ResponseEntity<>(
            new ErrorResponse(e.getStatus(), e.getMessage()),
            e.getStatus()
        );
    }

    // 5xx 서버 에러의 구체적인 내용을 클라이언트에게 알려줄 필요가 없음
    // -> 오히려 보안 측면에서 취약해질 수 있음
    // 단순하게 500 InternalServerError
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return  ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return  ResponseEntity.internalServerError().build();
    }



}
