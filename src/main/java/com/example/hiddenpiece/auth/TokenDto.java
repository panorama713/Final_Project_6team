package com.example.hiddenpiece.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {
    private String grantType;
    private String authorizationType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

    @Builder
    public TokenDto(String grantType, String authorizationType, String accessToken, String refreshToken, Long accessTokenExpiresIn) {
        this.grantType = grantType;
        this.authorizationType = authorizationType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }
}
