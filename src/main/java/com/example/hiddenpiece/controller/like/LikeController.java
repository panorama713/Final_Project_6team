package com.example.hiddenpiece.controller.like;

import com.example.hiddenpiece.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles/{articleId}/like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> likeArticle(@PathVariable Long articleId, Authentication authentication) {
        String username = authentication.getName();
        likeService.like(username, articleId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikeArticle(@PathVariable Long articleId, Authentication authentication) {
        String username = authentication.getName();
        likeService.unlike(username, articleId);
        return ResponseEntity.noContent().build();
    }
}
