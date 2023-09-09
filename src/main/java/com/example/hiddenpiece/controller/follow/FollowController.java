package com.example.hiddenpiece.controller.follow;

import com.example.hiddenpiece.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/follow")
public class FollowController {
    private final FollowService followService;

    @PostMapping
    public ResponseEntity<Void> follow(@RequestParam("usernameToFollow") String usernameToFollow, Authentication authentication) {
        System.out.println("\n\n\n\n\n\n\n\n"+usernameToFollow);
        String username = authentication.getName();
        followService.follow(usernameToFollow, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unfollow(@RequestParam("usernameToFollow") String usernameToFollow, Authentication authentication) {
        String username = authentication.getName();
        followService.unFollow(usernameToFollow, username);
        return ResponseEntity.noContent().build();
    }
}
