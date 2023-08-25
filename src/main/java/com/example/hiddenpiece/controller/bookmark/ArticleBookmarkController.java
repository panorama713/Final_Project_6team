package com.example.hiddenpiece.controller.bookmark;

import com.example.hiddenpiece.domain.dto.bookmark.RequestArticleBookmarkDto;
import com.example.hiddenpiece.domain.dto.bookmark.ResponseArticleBookmarkDto;
import com.example.hiddenpiece.service.bookmark.ArticleBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
public class ArticleBookmarkController {
    private final ArticleBookmarkService articleBookmarkService;

    @PostMapping("/articles/{articleId}")
    public ResponseEntity<ResponseArticleBookmarkDto> createArticleBookmark(
            @PathVariable Long articleId,
            Authentication authentication,
            @RequestBody RequestArticleBookmarkDto dto
    ) {
        String username = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(articleBookmarkService.createArticleBookmark(username, articleId, dto));
    }

    @GetMapping("/articles")
    public ResponseEntity<Page<ResponseArticleBookmarkDto>> readAllArticleByBookmark(
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "25") Integer limit
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(articleBookmarkService.readAllArticleByBookmark(username, page, limit));
    }

    @PutMapping("/{bookmarkId}/articles")
    public ResponseEntity<Void> updateBookmark(
            Authentication authentication,
            @PathVariable Long bookmarkId,
            @RequestBody RequestArticleBookmarkDto dto
    ) {
        String username = authentication.getName();
        articleBookmarkService.updateArticleBookmark(username, bookmarkId, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bookmarkId}/articles")
    public ResponseEntity<Void> deleteBookmark(
            Authentication authentication, @PathVariable Long bookmarkId
    ) {
        String username = authentication.getName();
        articleBookmarkService.deleteArticleBookmark(username, bookmarkId);
        return ResponseEntity.noContent().build();
    }
}
