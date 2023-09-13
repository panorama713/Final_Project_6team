package com.example.hiddenpiece.controller.comment;

import com.example.hiddenpiece.domain.dto.community.comment.CommentRequestDto;
import com.example.hiddenpiece.domain.dto.community.comment.CommentResponseDto;
import com.example.hiddenpiece.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    // 댓글 등록
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long articleId,
            @Valid @RequestBody CommentRequestDto dto,
            Authentication auth
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createComment(auth.getName(), articleId, dto));
    }

    // 댓글 및 답글 조회 - 게시글별
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> readAllCommentsForArticle(
            @PathVariable Long articleId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.readAllCommentsForArticle(articleId));
    }

    // 댓글 및 답글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto dto,
            Authentication auth
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.updateComment(auth.getName(), articleId, commentId, dto));
    }

    // 댓글 및 답글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            Authentication auth
    ) {
        commentService.deleteComment(auth.getName(), articleId, commentId);
        return ResponseEntity.noContent().build();
    }

    // 답글 등록
    @PostMapping("/{parentCommentId}/replies")
    public ResponseEntity<CommentResponseDto> createReply(
            @PathVariable Long articleId,
            @PathVariable Long parentCommentId,
            @Valid @RequestBody CommentRequestDto dto,
            Authentication auth
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createReply(auth.getName(), articleId, parentCommentId, dto));
    }

    // 댓글 및 답글 조회 - 사용자별, 페이지네이션
    @GetMapping("/getComments")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByUsername(
            @RequestParam(value = "page", defaultValue = "0") int page,
            Authentication auth
    ) {
        return ResponseEntity.ok(commentService.getCommentsByUsername(page, auth.getName()));
    }
}
