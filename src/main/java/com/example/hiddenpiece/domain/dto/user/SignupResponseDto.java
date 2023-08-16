package com.example.hiddenpiece.domain.dto.user;

import lombok.Data;

@Data
public class SignupResponseDto {
    private String message;

    public SignupResponseDto(String message) {
        this.message = message;
    }
}
