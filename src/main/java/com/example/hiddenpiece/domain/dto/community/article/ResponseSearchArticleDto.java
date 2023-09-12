package com.example.hiddenpiece.domain.dto.community.article;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseSearchArticleDto {
    private Long articleId;
    private String title;
    private String description;
    private String writer;

    public ResponseSearchArticleDto(Long articleId, String title, String description, String writer) {
        this.articleId = articleId;
        this.title = title;
        this.description = description;
        this.writer = writer;
    }
}
