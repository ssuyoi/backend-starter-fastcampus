package com.backendstarter.onlinecoffeesandbox.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리기
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * RuntimeException 발생 시 실패 Response
     */
    @ExceptionHandler(value = RuntimeException.class)
    public Response<Void> handleRuntimeException(RuntimeException e) {
        return Response.fail(e.getMessage());
    }
}
