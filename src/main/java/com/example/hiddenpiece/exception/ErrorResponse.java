package com.example.hiddenpiece.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
//    private final LocalDateTime timestamp = LocalDateTime.now();
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
                        .error(customExceptionCode.getHttpStatus().getReasonPhrase())
                        .message(customExceptionCode.getMessage())
                        .build());
    }
}
