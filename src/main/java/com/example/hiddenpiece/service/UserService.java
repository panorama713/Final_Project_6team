package com.example.hiddenpiece.service;

import com.example.hiddenpiece.domain.dto.user.SignupRequestDto;
import com.example.hiddenpiece.domain.dto.user.SignupResponseDto;
import com.example.hiddenpiece.domain.entity.User;
import com.example.hiddenpiece.domain.repository.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            log.warn("#log# 사용자 [{}] 등록 실패. 아이디 중복", requestDto.getUsername());
            throw new CustomException(CustomExceptionCode.ALREADY_EXIST_USER);
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("#log# 사용자 [{}] 등록 실패. 이메일 중복", requestDto.getEmail());
            throw new CustomException(CustomExceptionCode.ALREADY_EXIST_EMAIL);
        }

        User user = requestDto.toEntity(passwordEncoder);
        return new SignupResponseDto(userRepository.save(user));
    }
}
