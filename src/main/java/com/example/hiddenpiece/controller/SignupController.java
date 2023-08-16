package com.example.hiddenpiece.controller;

import com.example.hiddenpiece.domain.dto.user.SignupRequestDto;
import com.example.hiddenpiece.domain.dto.user.SignupResponseDto;
import com.example.hiddenpiece.service.SignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
@RestController
public class SignupController {
    private final SignupService signupService;

    /**
     * POST /signup
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(
            @Valid @RequestBody SignupRequestDto requestDto
    ) {
        log.info("#log# 사용자 [{}] 등록 시도", requestDto.getUsername());
        signupService.signup(requestDto);
        log.info("#log# 사용자 [{}] 등록 성공", requestDto.getUsername());
        return new ResponseEntity<>(new SignupResponseDto("회원가입이 완료되었습니다."), HttpStatus.OK);
    }
}
