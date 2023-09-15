package com.example.hiddenpiece.domain.dto.user;

import com.example.hiddenpiece.domain.entity.user.Question;
import com.example.hiddenpiece.domain.entity.user.Role;
import com.example.hiddenpiece.domain.entity.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.hiddenpiece.service.user.UserService.DEFAULT_PROFILE_IMG_PATH;

@Data
public class SignupRequestDto {
    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(regexp = "^[a-z][a-z0-9]{4,14}$", message = "아이디는 5~15자의 영문 소문자, 숫자만 사용 가능합니다.\"")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[`~₩!@#$%^&*]).{7,19}$", message = "비밀번호는 8~20자의 영문, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @NotBlank(message = "실명은 필수입니다.")
    private String realName;

    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "올바른 이메일 형식으로 입력해 주세요. (예: example@example.com)")
    private String email;

    @NotBlank(message = "휴대전화는 필수입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "올바른 전화번호 형식으로 입력해 주세요. (예: 01012345678)")
    private String phone;

    private Question question;

    @NotBlank(message = "보안 답변은 필수입니다.")
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
                .question(question)
                .answer(answer)
                .build();
    }
}
