package com.example.hiddenpiece.domain.dto.user;

import com.example.hiddenpiece.domain.entity.user.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestFindUsernameDto {
    @NotBlank(message = "실명은 필수입니다.")
    private String realName;

    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "올바른 이메일 형식으로 입력해 주세요. (예: example@example.com)")
    private String email;

    private Question question;

    @NotBlank(message = "보안 답변은 필수입니다.")
    private String answer;
}
