package com.backendstarter.crash.exception;

import com.backendstarter.crash.model.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

/**
 * 전역 예외를 처리 핸들러
 *
 * <p>모든 컨트롤러에서 발생하는 예외를 처리
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    /**
     * {@link ClientErrorException}(4xx) 계열의 커스텀 예외 처리
     *
     * @param e 발생한 {@link ClientErrorException}
     * @return 예외의 상태 코드와 메시지를 담은 {@link ErrorResponse}
     */
    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleClientErrorException(ClientErrorException e) {
        return new ResponseEntity<>(
            new ErrorResponse(e.getStatus(), e.getMessage()),
            e.getStatus()
        );
    }

    /**
     * {@code @Valid} 유효성 검사 실패에 대한 예외 처리
     *
     * @param e 발생한 {@link MethodArgumentNotValidException}
     * @return 에러 메시지를 담은 {@code 400 Bad Request} 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        var errorMessage = e.getFieldErrors().stream()
            .map(fieldError -> (fieldError.getField() + " : " + fieldError.getDefaultMessage()))
            .toList()
            .toString();
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage),HttpStatus.BAD_REQUEST);
    }

    /**
     * 요청 본문(Request Body)이 누락되었을 때 예외 처리
     *
     * @param e 발생한 {@link HttpMessageNotReadableException}
     * @return 고정 에러 메시지를 담은 {@code 400 Bad Request} 응답
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e) {

        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST, "Required request body is missing."), HttpStatus.BAD_REQUEST);
    }

    /**
     * 처리되지 않은 {@link RuntimeException} 예외 처리
     *
     * <p>서버 내부 오류의 상세 내용을 클라이언트에 노출하면 보안상 취약해질 수 있으므로
     * 응답 본문 없이 상태 코드만 반환
     *
     * @param e 발생한 {@link RuntimeException}
     * @return {@code 500 Internal Server Error} 응답
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return  ResponseEntity.internalServerError().build();
    }

    /**
     * {@link RuntimeException} 외 모든 {@link Exception}을 최종적으로 처리하는 폴백 핸들러
     *
     * <p>예상치 못한 Checked Exception 등이 이 핸들러에서 처리되며,
     * 마찬가지로 상세 내용은 노출하지 않고 {@code 500 Internal Server Error}만 반환
     *
     * @param e 발생한 {@link Exception}
     * @return {@code 500 Internal Server Error} 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return  ResponseEntity.internalServerError().build();
    }


}
