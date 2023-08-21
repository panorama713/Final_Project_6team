package com.example.hiddenpiece.domain.dto.community;

import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.community.ArticleType;
import com.example.hiddenpiece.domain.entity.community.Board;
import com.example.hiddenpiece.domain.entity.community.Category;
import com.example.hiddenpiece.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleRequestDto {
    private User user;
    private Category category;
    private String title;
    private String content;
    private ArticleType type;

    public Article toEntity() {
        return Article.builder()
                .user(user)
                .category(category)
                .title(title)
                .content(content)
                .type(type)
                .build();
    }
}
