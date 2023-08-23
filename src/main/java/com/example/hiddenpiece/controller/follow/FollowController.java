package com.example.hiddenpiece.controller.follow;

import com.example.hiddenpiece.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}")
public class FollowController {
    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@PathVariable Long userId, Authentication authentication) {
        String username = authentication.getName();
        followService.follow(userId, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<Void> unfollow(@PathVariable Long userId, Authentication authentication) {
        String username = authentication.getName();
        followService.unFollow(userId, username);
        return ResponseEntity.noContent().build();
    }
}
