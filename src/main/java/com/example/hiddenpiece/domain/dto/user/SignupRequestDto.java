package com.example.hiddenpiece.domain.dto.user;

import com.example.hiddenpiece.domain.entity.user.Role;
import com.example.hiddenpiece.domain.entity.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.hiddenpiece.service.user.UserService.DEFAULT_PROFILE_IMG_PATH;

@Data
public class SignupRequestDto {
    @NotBlank(message = "아이디는 필수입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

//    @NotBlank(message = "비밀번호 확인은 필수입니다.")
//    private String passwordCheck;
    // 이 부분은 프론트에서 처리

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
                .username(username)
                .password(passwordEncoder.encode(password))
                .realName(realName)
                .email(email)
                .phone(phone)
                .profileImg(DEFAULT_PROFILE_IMG_PATH)
                .role(Role.USER)
                .build();
    }
}
