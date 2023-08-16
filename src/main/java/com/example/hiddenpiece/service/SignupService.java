package com.example.hiddenpiece.service;

import com.example.hiddenpiece.domain.dto.user.SignupRequestDto;
import com.example.hiddenpiece.domain.entity.User;
import com.example.hiddenpiece.domain.repository.SignupRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class SignupService {
    private final SignupRepository signupRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    public User signup(SignupRequestDto requestDto) {
        if (signupRepository.existsByUsername(requestDto.getUsername())) {
            log.warn("#log# 사용자 [{}] 등록 실패. 아이디 중복", requestDto.getUsername());
            throw new CustomException(CustomExceptionCode.ALREADY_EXIST_USER);
        }
        if (signupRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("#log# 사용자 [{}] 등록 실패. 이메일 중복", requestDto.getEmail());
            throw new CustomException(CustomExceptionCode.ALREADY_EXIST_EMAIL);
        }
        if (!requestDto.getPassword().equals(requestDto.getPasswordCheck())) {
            log.warn("#log# 사용자 [{}] 등록 실패. 비밀번호 불일치", requestDto.getUsername());
            throw new CustomException(CustomExceptionCode.PASSWORD_CHECK_ERROR);
        }
        return signupRepository.save(requestDto.toEntity(passwordEncoder));
    }
}
