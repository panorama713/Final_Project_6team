package com.example.hiddenpiece.domain.dto.community;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.community.ArticleType;
import com.example.hiddenpiece.domain.entity.community.Board;
import com.example.hiddenpiece.domain.entity.community.Category;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleResponseDto {
    private Long articleId;
    private String user;
    private Category category;
    private String title;
    private String content;
    private ArticleType type;

    public ArticleResponseDto(Article entity) {
        this.articleId = entity.getId();
        this.user = entity.getUser().getUsername();
        this.category = entity.getCategory();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.type = entity.getType();
    }
}
