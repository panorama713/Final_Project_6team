package com.example.hiddenpiece.domain.dto.community.article;

import com.example.hiddenpiece.domain.entity.community.ArticleType;
import com.example.hiddenpiece.domain.entity.community.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleRequestDto {
    private String title;
    private String content;
    private Category category;
    private ArticleType type;
    private String imagePath;
}
