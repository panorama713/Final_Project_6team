package com.example.hiddenpiece.controller.comment;

import com.example.hiddenpiece.domain.dto.community.comment.CommentRequestDto;
import com.example.hiddenpiece.domain.dto.community.comment.CommentResponseDto;
import com.example.hiddenpiece.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/articles/{articleId}/comments")
@RestController
public class CommentController {
    private final CommentService commentService;

    private String getUsername(Authentication auth) {
        return auth.getName();
    }

    /**
     * POST
     * 댓글 등록
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long articleId,
            @Valid @RequestBody CommentRequestDto dto,
            Authentication auth
    ) {
        log.info("#log# 사용자 [{}]에 의해 게시글 아이디 [{}]에 댓글 등록 성공", getUsername(auth), articleId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createComment(getUsername(auth), articleId, dto));
    }

    /**
     * GET
     * 댓글 조회
     */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> readAllCommentsForArticle(
            @PathVariable Long articleId
    ) {
        log.info("#log# 게시글 아이디 [{}]의 모든 댓글 조회 성공", articleId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.readAllCommentsForArticle(articleId));
    }

    /**
     * PUT /{commentId}
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto dto,
            Authentication auth
    ) {
        log.info("#log# 사용자 [{}]에 의해 댓글 아이디 [{}] 수정 성공", getUsername(auth), commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.updateComment(getUsername(auth), commentId, dto));
    }

    /**
     * DELETE /{commentId}
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            Authentication auth
    ) {
        log.info("#log# 사용자 [{}]에 의해 댓글 아이디 [{}] 삭제 성공", getUsername(auth), commentId);
        commentService.deleteComment(getUsername(auth), commentId);
        return ResponseEntity.noContent().build();
    }
}
