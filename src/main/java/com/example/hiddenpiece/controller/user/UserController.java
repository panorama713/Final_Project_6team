package com.example.hiddenpiece.controller.user;

import com.example.hiddenpiece.domain.dto.user.*;
import com.example.hiddenpiece.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(
            @Valid @RequestBody SignupRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(requestDto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse res) {
        userService.logout(req, res);
        log.info("로그아웃 성공");
       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 토큰 재발급 -> 토큰 만료 시 재발급하는 로직 프론트 or 백에서 구현
    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(HttpServletRequest req, HttpServletResponse res) {
        userService.reissueAccessToken(req, res);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 유저 프로필 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDto> findUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.readUserProfile(userId));
    }

    // 나의 프로필 조회
    @GetMapping("/my-profile")
    public ResponseEntity<UserProfileResponseDto> findUserProfile(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.readMyProfile(username));
    }

    // 로그인 여부 체크
    @GetMapping("/check-login")
    public ResponseEntity<UserProfileResponseDto> checkLogin(HttpServletRequest req) {
        return ResponseEntity.ok(userService.checkLogin(req));
    }

    // 유저 인원 수 카운트
    @GetMapping("/count")
    public ResponseEntity<Integer> countUsers() {
        return ResponseEntity.ok(userService.countUsers());
    }

    // 아이디 찾기
    @PostMapping("/find/username")
    public ResponseEntity<String> findUsername(@Valid @RequestBody RequestFindUsernameDto dto) {
        return ResponseEntity.ok(userService.findUsername(dto));
    }

    // 비밀번호 찾기
    @PostMapping("/find/password")
    public ResponseEntity<Long> findPassword(@RequestBody RequestFindPasswordDto dto) {
        return ResponseEntity.ok(userService.findAccount(dto));
    }

    // 비밀번호 변경
    @PutMapping("/{userId}/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody RequestChangePasswordDto dto,
            @PathVariable Long userId
    ) {
        userService.updatePassword(dto, userId);
        return ResponseEntity.noContent().build();
    }
}
