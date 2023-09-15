package com.example.hiddenpiece.oauth;

import com.example.hiddenpiece.auth.JwtUtil;
import com.example.hiddenpiece.auth.TokenDto;
import com.example.hiddenpiece.domain.entity.user.Role;
import com.example.hiddenpiece.redis.RedisService;
import com.example.hiddenpiece.security.CookieManager;
import com.example.hiddenpiece.security.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2UserSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserDetailsManager userDetailsManager;
    private final RedisService redisService;
    private final CookieManager cookieManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        process(request, response, oAuth2User);
    }

    private void process(HttpServletRequest request, HttpServletResponse response, OAuth2User oAuth2User) throws IOException {
        log.info("Token 생성 시작");

        String email = oAuth2User.getAttribute("email");
        String username = "O_" + email.split("@")[0];
        String realName = oAuth2User.getAttribute("name");
        String provider = oAuth2User.getAttribute("provider");
        String providerId = oAuth2User.getAttribute("id").toString();
        String profileImg = oAuth2User.getAttribute("profile_img");

        if (!userDetailsManager.userExists(username)) {
            userDetailsManager.createUser(
                    new CustomUserDetails(
                            username, providerId, realName,
                            email, Role.USER, profileImg,
                            provider, providerId
                    )
            );
        }

        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        long refreshTokenExpirationMillis = jwtUtil.getRefreshTokenExpirationMillis();

        TokenDto tokenDto = jwtUtil.generateTokenDto((CustomUserDetails) userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        cookieManager.setCookie(response, accessToken, "accessToken", jwtUtil.getAccessTokenExpirationMillis() / 1000);
        cookieManager.setCookie(response, refreshToken, "refreshToken", jwtUtil.getRefreshTokenExpirationMillis() / 1000);

        redisService.setValues(username, refreshToken, Duration.ofMillis(refreshTokenExpirationMillis));

        String targetUrl = "/views/main";

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        clearAuthenticationAttributes(request);
    }
}
