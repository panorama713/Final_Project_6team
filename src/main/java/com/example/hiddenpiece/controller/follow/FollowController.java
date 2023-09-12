package com.example.hiddenpiece.controller.follow;

import com.example.hiddenpiece.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/follow")
public class FollowController {
    private final FollowService followService;

    @PostMapping
    public ResponseEntity<Void> follow(@PathVariable final Long userId, Authentication authentication) {
        String username = authentication.getName();
        followService.follow(userId, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unfollow(@PathVariable final Long userId, Authentication authentication) {
        String username = authentication.getName();
        followService.unFollow(userId, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Integer> getCountOfFollower (@PathVariable final Long userId) {
        int followNum = followService.getCountOfFollower(userId);
        return ResponseEntity.ok(followNum);
    }

    @GetMapping("/isFollow")
    public ResponseEntity<Boolean> isFollow ( Authentication authentication,
                                              @PathVariable final Long userId) {
        String username = authentication.getName();
        boolean isFollow = followService.isFollow(userId, username);
        return ResponseEntity.ok(isFollow);
    }
}
