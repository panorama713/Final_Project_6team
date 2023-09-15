package com.example.hiddenpiece.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성 검사에 발생하는 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("#log# MethodArgumentNotValidException 발생: [{}]", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ErrorResponse.errorResponse(e);
    }

    // 개발자의 요청에 발생하는 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("#log# CustomException 발생: [{}]", e.getExceptionCode().getMessage());
        return ErrorResponse.errorResponse(e.getExceptionCode());
    }
}
