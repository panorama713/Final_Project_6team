package com.example.hiddenpiece.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDto {
    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(regexp = "^[a-z][a-z0-9]{4,14}$", message = "아이디는 5~15자의 영문 소문자, 숫자만 사용 가능합니다.\"")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[`~₩!@#$%^&*]).{7,19}$", message = "비밀번호는 8~20자의 영문, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @Builder
    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
