package com.example.hiddenpiece.domain.dto.community.article;

import com.example.hiddenpiece.domain.entity.community.Article;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseFollowingArticlesDto {
    private Long articleId;
    private String writer;
    private String title;
    private String description;
    private LocalDateTime createdAt;

    @Builder
    public ResponseFollowingArticlesDto(Long articleId, String writer, String title, String description, LocalDateTime createdAt) {
        this.articleId = articleId;
        this.writer = writer;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static ResponseFollowingArticlesDto fromEntity(Article article) {
        return ResponseFollowingArticlesDto.builder()
                .articleId(article.getId())
                .writer(article.getUser().getUsername())
                .title(article.getTitle())
                .description(article.getContent())
                .createdAt(article.getCreatedAt())
                .build();
    }
}
