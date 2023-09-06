package com.example.hiddenpiece.domain.dto.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestFindUsernameDto {
    private String realName;
    private String email;
}
