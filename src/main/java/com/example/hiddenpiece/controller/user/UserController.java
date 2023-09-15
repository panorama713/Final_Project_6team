package com.example.hiddenpiece.controller.user;

import com.example.hiddenpiece.domain.dto.user.*;
import com.example.hiddenpiece.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService userService;

    // 회원가입
    @Operation(summary = "회원 가입 요청", description = "회원 가입 기능을 실행합니다.")
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(requestDto));
    }

    // 로그아웃
    @Operation(summary = "로그아웃 요청", description = "로그아웃 요청을 실행합니다.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse res) {
        userService.logout(req, res);
        log.info("로그아웃 성공");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 토큰 재발급 -> 토큰 만료 시 재발급하는 로직 프론트 or 백에서 구현
    @Operation(summary = "토큰 재발급 기능", description = "토큰을 재발급합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(HttpServletRequest req, HttpServletResponse res) {
        userService.reissueAccessToken(req, res);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 유저 프로필 조회
    @Operation(summary = "특정 유저의 프로필 조회 요청", description = "특정 유저의 프로필을 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDto> findUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.readUserProfile(userId));
    }

    // 나의 프로필 조회
    @Operation(summary = "나의 프로필 조회 요청", description = "나의 프로필을 조회합니다.")
    @GetMapping("/my-profile")
    public ResponseEntity<UserProfileResponseDto> findUserProfile(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.readMyProfile(username));
    }

    // 미니 프로필 조회
    @Operation(summary = "미니 프로필 조회 요청", description = "미니 프로필을 조회합니다.")
    @GetMapping("/mini-profile")
    public ResponseEntity<UserProfileResponseDto> checkLogin(HttpServletRequest req) {
        return ResponseEntity.ok(userService.readMiniProfile(req));
    }

    // 유저 인원 수 카운트
    @Operation(summary = "유저 인원 수 카운트 요청", description = "유저의 총 인원수를 조회합니다.")
    @GetMapping("/count")
    public ResponseEntity<Integer> countUsers() {
        return ResponseEntity.ok(userService.countUsers());
    }

    // 아이디 찾기
    @Operation(summary = "아이디 찾기 요청", description = "아이디 찾기 기능을 실행합니다.")
    @PostMapping("/find/username")
    public ResponseEntity<String> findUsername(@Valid @RequestBody RequestFindUsernameDto dto) {
        return ResponseEntity.ok(userService.findUsername(dto));
    }

    // 비밀번호 찾기
    @Operation(summary = "비밀번호 찾기 요청", description = "비밀번호 찾기 기능을 실행합니다.")
    @PostMapping("/find/password")
    public ResponseEntity<Long> findPassword(@RequestBody RequestFindPasswordDto dto) {
        return ResponseEntity.ok(userService.findAccount(dto));
    }

    // 비밀번호 변경
    @Operation(summary = "비밀번호 변경 요청", description = "비밀번호 변경 기능을 실행합니다.")
    @PutMapping("/{userId}/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody RequestChangePasswordDto dto,
            @PathVariable Long userId
    ) {
        userService.updatePassword(dto, userId);
        return ResponseEntity.noContent().build();
    }

    // 유저 정보 수정
    @Operation(summary = "유저 정보 수정 요청", description = "유저 정보 수정 기능을 실행합니다.")
    @PutMapping(value = "/{userId}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProfile(
            @PathVariable Long userId,
            @RequestPart(required = false, value = "profileImg") String profileImg,
            @RequestPart("dto") RequestUpdateUserInfoDto dto,
            Authentication authentication
    ) {
        String username = authentication.getName();
        userService.updateUserInfo(dto, profileImg, userId, username);
        return ResponseEntity.noContent().build();
    }

    // 회원 탈퇴
    @Operation(summary = "회원탈퇴 요청", description = "회원탈퇴 기능을 실행합니다.")
    @DeleteMapping("/{userId}/withdrawal")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId,
            @RequestBody RequestDeleteUserDto dto,
            Authentication authentication
    ) {
        String username = authentication.getName();
        userService.deleteUser(dto, username, userId);
        return ResponseEntity.noContent().build();
    }

    // 나의 팔로잉 목록 조회
    @Operation(summary = "내가 팔로우 하는 사람 목록 조회 요청", description = "내가 팔로우하는 사람 목록을 조회합니다.")
    @GetMapping("/{userId}/following-list")
    public ResponseEntity<Page<ResponseFollowingDto>> readFollowing(
            @PathVariable Long userId,
            Authentication authentication,
            @RequestParam("num") Integer num,
            @RequestParam("limit") Integer limit
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.readMyFollowings(num, limit, username, userId));
    }

    // 나의 팔로워 목록 조회
    @Operation(summary = "나를 팔로우하는 사람 목록 조회 요청", description = "나를 팔로우하는 사람 목록을 조회합니다.")
    @GetMapping("/{userId}/follower-list")
    public ResponseEntity<Page<ResponseFollowerDto>> readFollower(
            @PathVariable Long userId,
            Authentication authentication,
            @RequestParam("num") Integer num,
            @RequestParam("limit") Integer limit
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.readMyFollowers(num, limit, username, userId));
    }
}
