package com.example.hiddenpiece.controller.community;
import com.example.hiddenpiece.domain.dto.community.article.ArticleRequestDto;
import com.example.hiddenpiece.domain.dto.community.article.ArticleListResponseDto;
import com.example.hiddenpiece.domain.dto.community.article.ArticleResponseDto;
import com.example.hiddenpiece.domain.dto.community.article.CreateArticleResponseDto;
import com.example.hiddenpiece.service.community.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<CreateArticleResponseDto> createArticle(
            Authentication authentication, @RequestBody final ArticleRequestDto params
    ) {
        String username = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(username, params));
    }

    @GetMapping
    public ResponseEntity<List<ArticleListResponseDto>> readAllArticles() {
        return ResponseEntity.ok(articleService.readArticles());
    }

    // 게시글 단독 조회 (좋아요 개수 포함)
    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponseDto> readArticle(@PathVariable final Long articleId) {
        return ResponseEntity.ok(articleService.readArticle(articleId));
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<Void> updateArticle(
            Authentication authentication,
            @PathVariable final Long articleId,
            @RequestBody final ArticleRequestDto params
    ) {
        String username = authentication.getName();
        articleService.updateArticle(username, articleId, params);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(Authentication authentication, @PathVariable final Long articleId) {
        String username = authentication.getName();
        articleService.deleteArticle(username, articleId);
        return ResponseEntity.noContent().build();
    }
}
