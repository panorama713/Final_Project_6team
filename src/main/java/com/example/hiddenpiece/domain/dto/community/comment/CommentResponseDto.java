package com.example.hiddenpiece.domain.dto.community.comment;

import com.example.hiddenpiece.domain.entity.comment.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    @JsonProperty("isWriter")
    private Boolean isWriter;

    private String articleWriter;

    @JsonProperty("isArticleWriter")
    private Boolean isArticleWriter;

    public static CommentResponseDto fromEntity(Comment entity) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isCurrentWriter = entity.getUser().getUsername().equals(currentUsername);

        String articleWriterUsername = entity.getArticle().getUser().getUsername();
        boolean isCommentArticleWriter = entity.getUser().getUsername().equals(articleWriterUsername);

        return CommentResponseDto.builder()
                .id(entity.getId())
                .username(entity.getUser().getUsername())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .lastModifiedAt(entity.getLastModifiedAt())
                .isWriter(isCurrentWriter)
                .isArticleWriter(isCommentArticleWriter)
                .articleWriter(articleWriterUsername)
                .build();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL) // 대댓글의 대댓글 목록을 JSON 응답에서 제외
    private List<CommentResponseDto> replies;

    public void setReplies(List<CommentResponseDto> replies) {
        if (replies != null && !replies.isEmpty()) {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            String articleWriterUsername = this.articleWriter;

            for (CommentResponseDto reply : replies) {
                reply.setIsWriter(reply.getUsername().equals(currentUsername));
                reply.setIsArticleWriter(reply.getUsername().equals(articleWriterUsername));
            }
        }

        this.replies = replies;
    }

    public void setIsWriter(boolean isWriter) {
        this.isWriter = isWriter;
    }

    public void setIsArticleWriter(boolean isArticleWriter) {
        this.isArticleWriter = isArticleWriter;
    }
}
