package com.example.hiddenpiece.domain.dto.user;

import com.example.hiddenpiece.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupResponseDto {
    private String realName;
    private String username;

    @Builder
    public SignupResponseDto(User user) {
        this.realName = user.getRealName();
        this.username = user.getUsername();
    }
}
