package com.example.hiddenpiece.exception;

import com.example.hiddenpiece.domain.dto.user.SignupResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성 검사에 발생하는 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SignupResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("#log# MethodArgumentNotValidException 발생: [{}]", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<>(new SignupResponseDto(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

    // 사용자의 요청에 발생하는 예외
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<SignupResponseDto> handleResponseStatusException(ResponseStatusException e) {
        log.error("#log# ResponseStatusException 발생: [{}]", e.getReason());
        return new ResponseEntity<>(new SignupResponseDto(e.getReason()), e.getStatusCode());
    }
}
