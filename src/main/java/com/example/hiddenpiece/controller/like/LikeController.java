package com.example.hiddenpiece.controller.like;

import com.example.hiddenpiece.service.like.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles/{articleId}/like")
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "게시글 좋아요 요청", description = "게시글 좋아요 요청을 실행합니다.")
    @PostMapping
    public ResponseEntity<Void> likeArticle(@PathVariable Long articleId, Authentication authentication) {
        String username = authentication.getName();
        likeService.like(username, articleId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 좋아요 취소 요청", description = "게시글 좋아요 취소 기능을 실행합니다.")
    @DeleteMapping
    public ResponseEntity<Void> unlikeArticle(@PathVariable Long articleId, Authentication authentication) {
        String username = authentication.getName();
        likeService.unlike(username, articleId);
        return ResponseEntity.noContent().build();
    }
}
