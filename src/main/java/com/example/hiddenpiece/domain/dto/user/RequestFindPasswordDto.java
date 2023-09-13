package com.example.hiddenpiece.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestFindPasswordDto {
    @NotBlank(message = "실명은 필수입니다.")
    private String realName;

    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(regexp = "^[a-z][a-z0-9]{4,14}$", message = "아이디는 5~15자의 영문 소문자, 숫자만 사용 가능합니다.\"")
    private String username;
}
