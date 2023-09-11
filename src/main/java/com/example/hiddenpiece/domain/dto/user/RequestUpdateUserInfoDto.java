package com.example.hiddenpiece.domain.dto.user;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RequestUpdateUserInfoDto {
    private String password;
    private String email;
    private String phone;
//    private String profileImg;
}