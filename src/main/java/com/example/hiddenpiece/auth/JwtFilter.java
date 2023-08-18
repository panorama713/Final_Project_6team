package com.example.hiddenpiece.auth;

import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.ErrorResponse;
import com.example.hiddenpiece.redis.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final List<String> EXCLUDE_URL =
            List.of("/api/*/users/login",
                    "/api/*/users/signup",
                    "/api/*/users/reissue");

    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtUtil.resolveAccessToken(request);
            if (StringUtils.hasText(accessToken) && doNotLogout(accessToken) && jwtUtil.validate(accessToken)) {
                setAuthentication(accessToken);
            }
        } catch (RuntimeException e) {
            if (e instanceof CustomException) {
                ObjectMapper objectMapper = new ObjectMapper();
                ErrorResponse errorResponse = new ErrorResponse(((CustomException) e).getExceptionCode().name(), e.getMessage());

                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                response.setStatus(((CustomException) e).getExceptionCode().getHttpStatus().value());
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        boolean isExcluded = EXCLUDE_URL.stream()
                .anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));

        return isExcluded;
    }

    private boolean doNotLogout(String accessToken) {
        String isLogout = redisService.getValues(accessToken);
        return isLogout.equals("false");
    }

    public void setAuthentication(String accessToken) {
        Authentication authentication = jwtUtil.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
