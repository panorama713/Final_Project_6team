package com.example.hiddenpiece.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private final String error;
    private final String message;

    public static ResponseEntity<ErrorResponse> errorResponse(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .error(HttpStatus.BAD_REQUEST.name())
                        .message(e.getAllErrors().get(0).getDefaultMessage())
                        .build());
    }

    public static ResponseEntity<ErrorResponse> errorResponse(CustomExceptionCode customExceptionCode) {
        return ResponseEntity
                .status(customExceptionCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .error(customExceptionCode.name())
                        .message(customExceptionCode.getMessage())
                        .build());
    }
}
