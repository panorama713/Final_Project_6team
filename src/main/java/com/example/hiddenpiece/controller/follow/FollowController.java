package com.example.hiddenpiece.controller.follow;

import com.example.hiddenpiece.service.follow.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/follow")
public class FollowController {
    private final FollowService followService;

    @Operation(summary = "팔로우 요청", description = "팔로우 기능을 실행합니다.")
    @PostMapping
    public ResponseEntity<Void> follow(@PathVariable final Long userId, Authentication authentication) {
        String username = authentication.getName();
        followService.follow(userId, username);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "언팔로우 요청", description = "언팔로우 기능을 실행합니다.")
    @DeleteMapping
    public ResponseEntity<Void> unfollow(@PathVariable final Long userId, Authentication authentication) {
        String username = authentication.getName();
        followService.unFollow(userId, username);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "팔로잉 숫자 요청", description = "나를 팔로잉한 사람의 수를 조회하는 기능을 실행합니다.")
    @GetMapping
    public ResponseEntity<Integer> getCountOfFollower(@PathVariable final Long userId) {
        int followNum = followService.getCountOfFollower(userId);
        return ResponseEntity.ok(followNum);
    }

    @Operation(summary = "팔로우 숫자 기능 요청", description = "내가 팔로우한 사람의 수를 조회하는 기능을 실행합니다.")
    @GetMapping("/isFollow")
    public ResponseEntity<Boolean> isFollow(@PathVariable final Long userId, Authentication authentication) {
        String username = authentication.getName();
        boolean isFollow = followService.isFollow(userId, username);
        return ResponseEntity.ok(isFollow);
    }
}
