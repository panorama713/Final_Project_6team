package com.example.hiddenpiece.security;

import com.example.hiddenpiece.auth.JwtUtil;
import com.example.hiddenpiece.auth.TokenDto;
import com.example.hiddenpiece.domain.dto.user.LoginDto;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import com.example.hiddenpiece.redis.RedisService;
import com.example.hiddenpiece.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.example.hiddenpiece.security.CookieManager.*;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserService userService;
    private final RedisService redisService;
    private final AuthenticationManager authenticationManager;
    private final CookieManager cookieManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공");
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        TokenDto tokenDto = jwtUtil.generateTokenDto(customUserDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        long accessTokenExpirationMillis = jwtUtil.getAccessTokenExpirationMillis();
        long refreshTokenExpirationMillis = jwtUtil.getRefreshTokenExpirationMillis();
        cookieManager.setCookie(response, accessToken, ACCESS_TOKEN, accessTokenExpirationMillis);
        cookieManager.setCookie(response, refreshToken, REFRESH_TOKEN, refreshTokenExpirationMillis);
        // User 찾기
        User user = userService.findUserAndCheckUserExists(customUserDetails.getId());
        redisService.setValues(user.getUsername(), refreshToken, Duration.ofMillis(refreshTokenExpirationMillis));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.error("로그인 실패");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("errorCode", CustomExceptionCode.USER_NOT_MATCH.name());
        result.put("message", CustomExceptionCode.USER_NOT_MATCH.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
