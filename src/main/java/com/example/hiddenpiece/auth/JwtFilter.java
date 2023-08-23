package com.example.hiddenpiece.auth;

import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.redis.RedisService;
import com.example.hiddenpiece.security.CookieManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final List<String> EXCLUDE_URL =
            List.of("/api/v1/users/login",
                    "/api/v1/users/signup",
                    "/api/v1/users/reissue",
                    "/views/main",
                    "/views/login");

    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final CookieManager cookieManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = cookieManager.getCookie(request, CookieManager.ACCESS_TOKEN);
            if (StringUtils.hasText(accessToken) && doNotLogout(accessToken)) {
                if (jwtUtil.validate(accessToken)) {
                    setAuthentication(accessToken);
                }
            }
        } catch (CustomException e) {
            request.setAttribute("exception", e.getExceptionCode());
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDE_URL.stream()
                .anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }

    private boolean doNotLogout(String accessToken) {
        String isLogout = redisService.getValues(accessToken);
        return isLogout.equals("false");
    }

    public void setAuthentication(String accessToken) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.getAuthentication(accessToken);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
