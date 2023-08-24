package com.example.hiddenpiece.domain.dto.community.article;

import com.example.hiddenpiece.domain.entity.community.Article;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CreateArticleResponseDto {
    private Long id;
    private String username;
    private String title;

    public static CreateArticleResponseDto fromEntity(Article article) {
        return CreateArticleResponseDto.builder()
                .id(article.getId())
                .username(article.getUser().getUsername())
                .title(article.getTitle())
                .build();
    }
}
