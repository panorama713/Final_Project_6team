package com.example.hiddenpiece.controller.follow;

import com.example.hiddenpiece.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @GetMapping
    public ResponseEntity<Integer> getCountOfFollower (@RequestParam("username") String username) {
        int followNum = followService.getCountOfFollower(username);
        return ResponseEntity.ok(followNum);
    }

    @GetMapping("/isFollow")
    public ResponseEntity<Boolean> isFollow ( Authentication authentication,
                                              @RequestParam("writer") String writer) {
        String username = authentication.getName();
        boolean isFollow = followService.isFollow(writer, username);
        return ResponseEntity.ok(isFollow);
    }
}
