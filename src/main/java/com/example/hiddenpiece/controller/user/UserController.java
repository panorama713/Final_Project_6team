package com.example.hiddenpiece.controller.user;

import com.example.hiddenpiece.domain.dto.user.SignupRequestDto;
import com.example.hiddenpiece.domain.dto.user.SignupResponseDto;
import com.example.hiddenpiece.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(HttpServletRequest req, HttpServletResponse res) {
        userService.reissueAccessToken(req, res);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
