package com.example.hiddenpiece.controller.bookmark;

import com.example.hiddenpiece.domain.dto.bookmark.RequestArticleBookmarkDto;
import com.example.hiddenpiece.domain.dto.bookmark.ResponseArticleBookmarkDto;
import com.example.hiddenpiece.service.bookmark.ArticleBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "북마크 생성 요청", description = "북마크 생성 기능을 실행합니다.")
    @PostMapping("/articles/{articleId}")
    public ResponseEntity<ResponseArticleBookmarkDto> createArticleBookmark(
            @PathVariable Long articleId,
            Authentication authentication,
            @RequestBody RequestArticleBookmarkDto dto
    ) {
        String username = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(articleBookmarkService.createArticleBookmark(username, articleId, dto));
    }

    @Operation(summary = "특정 유저의 게시글 북마크 전체 조회 요청", description = "특정 유저가 북마크한 게시글을 조회하는 기능을 실행합니다.")
    @GetMapping("/articles")
    public ResponseEntity<Page<ResponseArticleBookmarkDto>> readAllArticleByBookmark(
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(articleBookmarkService.readAllArticleByBookmark(username, page));
    }

    @Operation(summary = "게시글 북마크 수정 요청", description = "게시글 북마크를 수정하는 기능을 실행합니다.")
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

    @Operation(summary = "게시글 북마크 삭제 요청", description = "게시글 북마크를 삭제하는 기능을 실행합니다.")
    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<Void> deleteBookmark(
            Authentication authentication, @PathVariable Long articleId
    ) {
        String username = authentication.getName();
        articleBookmarkService.deleteArticleBookmark(username, articleId);
        return ResponseEntity.noContent().build();
    }
}
