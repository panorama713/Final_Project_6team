package com.example.hiddenpiece.service.user;

import com.example.hiddenpiece.auth.JwtUtil;
import com.example.hiddenpiece.auth.TokenDto;
import com.example.hiddenpiece.domain.dto.user.SignupRequestDto;
import com.example.hiddenpiece.domain.dto.user.SignupResponseDto;
import com.example.hiddenpiece.domain.dto.user.UserProfileResponseDto;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import com.example.hiddenpiece.redis.RedisService;
import com.example.hiddenpiece.security.CookieManager;
import com.example.hiddenpiece.security.CustomUserDetails;
import com.example.hiddenpiece.service.follow.FollowService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.example.hiddenpiece.exception.CustomExceptionCode.*;
import static com.example.hiddenpiece.security.CookieManager.*;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    private final CookieManager cookieManager;
    private final FollowService followService;

    // 회원가입
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

    // 로그아웃
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 access token 불러오기
        String accessToken = cookieManager.getCookie(request, ACCESS_TOKEN);

        // redis 에서 토큰 찾기 위해 유저 정보 탐색
        Claims claims = jwtUtil.parseClaims(accessToken);
        if (claims == null) {
            throw new CustomException(EXPIRED_JWT);
        }

        String username = claims.getSubject();
        String redisRefreshToken = redisService.getValues(username);

        // 토큰 관련 쿠키 제거
        cookieManager.deleteCookie(response, ACCESS_TOKEN);
        cookieManager.deleteCookie(response, REFRESH_TOKEN);

        // redis 에 있는 refresh token 지우고 access token 블랙리스트 등록
        if (redisService.checkExistsValue(redisRefreshToken)) {
            redisService.deleteValuesByKey(username);

            long accessTokenExpirationMillis = jwtUtil.getAccessTokenExpirationMillis();
            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
        }
    }

    // 토큰 재발급
    @Transactional
    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieManager.getCookie(request, REFRESH_TOKEN);

        verifiedRefreshToken(refreshToken);

        try {
            Claims claims = jwtUtil.parseClaims(refreshToken);
            String username = claims.getSubject();
            String redisRefreshToken = redisService.getValues(username);

            if (redisService.checkExistsValue(redisRefreshToken) && refreshToken.equals(redisRefreshToken)) {
                User user = findUserByUsername(username);
                CustomUserDetails userDetails = CustomUserDetails.of(user);

                TokenDto tokenDto = jwtUtil.generateTokenDto(userDetails);
                String newAccessToken = tokenDto.getAccessToken();

                long accessTokenExpirationMillis = jwtUtil.getAccessTokenExpirationMillis();

                cookieManager.setCookie(response, newAccessToken, ACCESS_TOKEN, accessTokenExpirationMillis);
            }
        } catch (Exception e) {
            throw new CustomException(REISSUE_FAILED);
        }
    }

    // 유저 프로필 조회
    public UserProfileResponseDto readUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        return UserProfileResponseDto.builder()
                .username(user.getUsername())
                .realName(user.getRealName())
                .email(user.getEmail())
                .numberOfWrittenArticle(0)     // TODO 기능 구현시 구현 예정
                .numberOfWrittenComment(0)     // TODO 기능 구현시 구현 예정
                .followingCount(followService.getCountOfFollowing(user))
                .followerCount(followService.getCountOfFollower(user))
                .build();
    }

    // TODO 마이 프로필 구현

    private void verifiedRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new CustomException(CustomExceptionCode.REFRESH_TOKEN_NOT_EXISTS);
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
