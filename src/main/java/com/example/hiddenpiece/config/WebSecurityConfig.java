package com.example.hiddenpiece.config;

import com.example.hiddenpiece.auth.JwtFilter;
import com.example.hiddenpiece.auth.JwtUtil;
import com.example.hiddenpiece.domain.entity.user.Role;
import com.example.hiddenpiece.oauth.OAuth2UserSuccessHandler;
import com.example.hiddenpiece.redis.RedisService;
import com.example.hiddenpiece.security.CookieManager;
import com.example.hiddenpiece.security.CustomAccessDeniedHandler;
import com.example.hiddenpiece.security.CustomAuthenticationEntryPoint;
import com.example.hiddenpiece.security.LoginFilter;
import com.example.hiddenpiece.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RedisService redisService;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
    private final OAuth2UserSuccessHandler oAuth2UserSuccessHandler;
    private final CookieManager cookieManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/users/signup",
                                "/api/v1/users/login",
                                "/api/v1/users/reissue",
                                "/api/v1/users/find/username",
                                "/api/v1/users/find/password",
                                "/api/v1/users/*/change-password",
                                "/api/v1/roadmaps/count",
                                "/api/v1/users/count",
                                "/api/v1/roadmaps/top5",
                                "/api/v1/roadmaps/total-search/**",
                                "/api/v1/articles/total-search/**",
                                "/views/**",
                                "/static/**",
                                "/uploads/**"
                                // 임시
                                ,"/api/v1/roadmap"
                        )
                        .permitAll()
                        .requestMatchers("/api/v1/roadmaps/**").authenticated()
                        .anyRequest().hasAuthority("ROLE_" + Role.USER.name()))
                .oauth2Login(oauth -> oauth
                        .loginPage("/views/login")
                        .successHandler(oAuth2UserSuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .apply(new CustomFilterConfigurer());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 모든 접근 가능
        configuration.addAllowedOriginPattern("*");
        // HTTP method 지정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        // Access-Control-Allow-Credentials 헤더 설정
        configuration.setAllowCredentials(true);
        // 클라이언트가 전송할 수 있는 헤더 값을 설정
        configuration.addAllowedHeader("*");
        // 클라이언트가 다시 preflight 요청을 보내지 않아도 되는 시간을 설정
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로 CORS 적용
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) {
            log.info("SecurityConfiguration.CustomFilterConfigurer.configure execute");
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            LoginFilter loginFilter = new LoginFilter(
                    jwtUtil, userService, redisService, authenticationManager, cookieManager);
            JwtFilter jwtFilter = new JwtFilter(jwtUtil, redisService, cookieManager);

            loginFilter.setFilterProcessesUrl("/api/*/users/login");

            http.addFilter(loginFilter)
                .addFilterAfter(jwtFilter, LoginFilter.class);
        }
    }
}
