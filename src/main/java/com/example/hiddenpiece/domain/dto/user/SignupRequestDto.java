package com.example.hiddenpiece.domain.dto.user;

import com.example.hiddenpiece.domain.entity.Role;
import com.example.hiddenpiece.domain.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class SignupRequestDto {
    @NotBlank(message = "아이디는 필수입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String passwordCheck;

    @NotBlank(message = "실명은 필수입니다.")
    private String realName;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식으로 입력해 주세요. (예: example@example.com)")
    private String email;

    @NotBlank(message = "휴대전화는 필수입니다.")
    private String phone;

    private String question;
    private String answer;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(this.username)
                .password(passwordEncoder.encode(this.password))
                .realName(this.realName)
                .email(this.email)
                .phone(this.phone)
                .role(Role.ROLE_USER)
                .build();
    }
}
