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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.hiddenpiece.exception.CustomExceptionCode.*;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    // 댓글 등록
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
        return CommentResponseDto.fromEntity(savedComment);
    }

    // 댓글 및 답글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> readAllCommentsForArticle(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        return comments.isEmpty() ? new ArrayList<>() : comments.stream()
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

    // 댓글 및 답글 수정
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
        return CommentResponseDto.fromEntity(comment);
    }

    // 댓글 및 답글 삭제
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
    }

    // 답글 등록
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
        return CommentResponseDto.fromEntity(savedReply);
    }

    public Page<CommentResponseDto> getCommentsByUsername(int page, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        Page<Comment> commentPage = commentRepository.findCommentsByUser(user, pageable);
        return commentPage.map(CommentResponseDto::fromEntity);
    }
}