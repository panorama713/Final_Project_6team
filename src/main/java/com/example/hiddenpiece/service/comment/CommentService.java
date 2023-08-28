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

import java.util.ArrayList;
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
        log.info("#log# 데이터베이스 저장 - 사용자 [{}] -> 게시글 [{}] -> 댓글 [{}]", username, articleId, savedComment.getId());
        return CommentResponseDto.fromEntity(savedComment);
    }

    /**
     * 댓글 및 대댓글 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> readAllCommentsForArticle(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        if (comments.isEmpty()) {
            return new ArrayList<>();
        }
        log.info("#log# 데이터베이스 조회 - 게시글 [{}] -> (대)댓글", articleId);
        return comments.stream()
                .filter(comment -> comment.getParentComment() == null)
                .map(mainComment -> {
                    CommentResponseDto dto = CommentResponseDto.fromEntity(mainComment);
                    List<CommentResponseDto> replies = mainComment.getChildComments().stream()
                            .map(reply -> {
                                CommentResponseDto replyDto = CommentResponseDto.fromEntity(reply);
                                replyDto.setReplies(null); // 대댓글의 대댓글 목록을 null로 설정하여 JSON 변환 시 제외
                                return replyDto;
                            })
                            .collect(Collectors.toList());
                    dto.setReplies(replies);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 댓글 및 대댓글 수정
     */
    @Transactional
    public CommentResponseDto updateComment(
            String username, Long articleId, Long commentId, CommentRequestDto dto
    ) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));
        if (!comment.getUser().getUsername().equals(username)) {
            throw new CustomException(NOT_MATCH_WRITER);
        }
        comment.update(dto.getContent());
        log.info("#log# 데이터베이스 수정 - 사용자 [{}] -> 게시글 [{}] -> (대)댓글 [{}]", username, articleId, commentId);
        return CommentResponseDto.fromEntity(comment);
    }

    /**
     * 댓글 및 대댓글 삭제
     */
    @Transactional
    public void deleteComment(
            String username, Long articleId, Long commentId
    ) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));
        if (!comment.getUser().getUsername().equals(username)) {
            throw new CustomException(NOT_MATCH_WRITER);
        }
        commentRepository.deleteById(commentId);
        log.info("#log# 데이터베이스 소프트 삭제 - 사용자 [{}] -> 게시글 [{}] -> (대)댓글 [{}]", username, articleId, commentId);
    }

    /**
     * 대댓글 등록
     */
    @Transactional
    public CommentResponseDto createReply(
            String username, Long articleId, Long parentCommentId, CommentRequestDto dto
    ) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));
        if (parentComment.getParentComment() != null) {
            throw new CustomException(NOT_ALLOW_MORE_REPLY);
        }
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE));
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        Comment reply = Comment.builder()
                .user(loginUser)
                .article(article)
                .content(dto.getContent())
                .parentComment(parentComment)
                .build();
        Comment savedReply = commentRepository.save(reply);
        log.info("#log# 데이터베이스 저장 - 사용자 [{}] -> 게시글 [{}] -> 댓글 [{}] -> 대댓글 [{}]", username, articleId, parentCommentId, savedReply.getId());
        return CommentResponseDto.fromEntity(savedReply);
    }
}
