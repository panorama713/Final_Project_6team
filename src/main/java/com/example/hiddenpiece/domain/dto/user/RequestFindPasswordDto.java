package com.example.hiddenpiece.domain.dto.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestFindPasswordDto {
    private String realName;
    private String username;
}
