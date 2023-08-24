package com.example.hiddenpiece.service.comment;

import com.example.hiddenpiece.domain.dto.community.comment.CommentRequestDto;
import com.example.hiddenpiece.domain.dto.community.comment.CommentResponseDto;
import com.example.hiddenpiece.domain.entity.comment.Comment;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.comment.CommentRepository;
import com.example.hiddenpiece.domain.repository.community.ArticleRepository;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.hiddenpiece.exception.CustomExceptionCode.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    /**
     * 댓글 등록
     */
    @Transactional
    public CommentResponseDto createComment(
            String username, Long articleId, CommentRequestDto dto
    ) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE));
        Comment comment = Comment.builder()
                .user(loginUser)
                .article(article)
                .content(dto.getContent())
                .build();
        Comment savedComment = commentRepository.save(comment);
        log.info("#log# 게시글 아이디 [{}]의 댓글 아이디 [{}] 데이터베이스 저장", articleId, savedComment.getId());
        return CommentResponseDto.fromEntity(savedComment);
    }

    /**
     * 댓글 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> readAllCommentsForArticle(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        if (comments.isEmpty()) {
            throw new CustomException(NOT_FOUND_COMMENT);
        }
        log.info("#log# 게시글 아이디 [{}]의 모든 댓글 데이터베이스 조회", articleId);
        return comments.stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public CommentResponseDto updateComment(
            String username, Long commentId, CommentRequestDto dto
    ) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));
        if (!comment.getUser().getUsername().equals(username)) {
            throw new CustomException(NOT_MATCH_WRITER);
        }
        comment.update(dto.getContent());
        log.info("#log# 사용자 [{}]의 댓글 아이디 [{}] 데이터베이스 수정", username, commentId);
        return CommentResponseDto.fromEntity(comment);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(
            String username, Long commentId
    ) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));
        if (!comment.getUser().getUsername().equals(username)) {
            throw new CustomException(NOT_MATCH_WRITER);
        }
        comment.softDelete();
        log.info("#log# 사용자 [{}]의 댓글 아이디 [{}] 데이터베이스 소프트 삭제", username, commentId);
    }

    /**
     * 대댓글 등록
     */
    @Transactional
    public CommentResponseDto createReply(
            String username, Long parentCommentId, CommentRequestDto dto
    ) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));
        if(parentComment.getParentComment() != null) {
            throw new CustomException(NOT_ALLOW_MORE_REPLY);
        }
        Comment reply = Comment.builder()
                .user(loginUser)
                .article(parentComment.getArticle())
                .content(dto.getContent())
                .parentComment(parentComment)
                .build();
        Comment savedReply = commentRepository.save(reply);
        log.info("#log# 댓글 아이디 [{}]의 대댓글 아이디 [{}] 데이터베이스 저장", parentCommentId, savedReply.getId());
        return CommentResponseDto.fromEntity(savedReply);
    }
}
