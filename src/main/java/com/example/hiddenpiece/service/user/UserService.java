package com.example.hiddenpiece.service.user;

import com.example.hiddenpiece.auth.JwtUtil;
import com.example.hiddenpiece.auth.TokenDto;
import com.example.hiddenpiece.domain.dto.user.SignupRequestDto;
import com.example.hiddenpiece.domain.dto.user.SignupResponseDto;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import com.example.hiddenpiece.redis.RedisService;
import com.example.hiddenpiece.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.example.hiddenpiece.exception.CustomExceptionCode.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            log.warn("#log# 사용자 [{}] 등록 실패. 아이디 중복", requestDto.getUsername());
            throw new CustomException(ALREADY_EXIST_USER);
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("#log# 사용자 [{}] 등록 실패. 이메일 중복", requestDto.getEmail());
            throw new CustomException(ALREADY_EXIST_EMAIL);
        }

        User user = requestDto.toEntity(passwordEncoder);
        return new SignupResponseDto(userRepository.save(user));
    }

    @Transactional
    public void logout(String refreshToken, String accessToken) {
        verifiedRefreshToken(refreshToken);
        Claims claims = jwtUtil.parseClaims(accessToken);
        String username = claims.getSubject();
        String redisRefreshToken = redisService.getValues(username);
        if (redisService.checkExistsValue(redisRefreshToken)) {
            redisService.deleteValues(username);

            long accessTokenExpirationMillis = jwtUtil.getAccessTokenExpirationMillis();
            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
        }
    }

    @Transactional
    public String reissueAccessToken(String refreshToken) {
        verifiedRefreshToken(refreshToken);
        Claims claims = jwtUtil.parseClaims(refreshToken);
        String username = claims.getSubject();
        if (username.contains("_")) {
            username = username.substring(2);
        }

        String redisRefreshToken = redisService.getValues(username);

        if (redisService.checkExistsValue(redisRefreshToken) && refreshToken.equals(redisRefreshToken)) {
            User user = findUserByUsername(username);
            CustomUserDetails userDetails = CustomUserDetails.of(user);
            TokenDto tokenDto = jwtUtil.generateTokenDto(userDetails);
            String newAccessToken = tokenDto.getAccessToken();
            String newRefreshToken = tokenDto.getRefreshToken();
            long refreshTokenExpirationMillis = jwtUtil.getRefreshTokenExpirationMillis();
            redisService.setValues(username, newRefreshToken, Duration.ofMillis(refreshTokenExpirationMillis));

            return newAccessToken;
        } else throw new CustomException(CustomExceptionCode.TOKEN_NOT_MATCH);
    }

    // TODO 마이페이지 로직 구현

    private void verifiedRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new CustomException(CustomExceptionCode.HEADER_REFRESH_TOKEN_NOT_EXISTS);
        }
    }

    public User findUserAndCheckUserExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    }
}
