package com.example.hiddenpiece.domain.dto.community.article;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.community.ArticleType;
import com.example.hiddenpiece.domain.entity.community.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleListResponseDto {
    private Long id;
    private String username;
    private String title;
    private Category category;
    private ArticleType type;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private int viewCount;
    private boolean hasImage;
    private int commentCount;

    public ArticleListResponseDto(Article entity, int commentCount) {
        this.id = entity.getId();
        this.username = entity.getUser().getUsername();
        this.title = entity.getTitle();
        this.category = entity.getCategory();
        this.type = entity.getType();
        this.likeCount = entity.getLikeArticles().size();
        this.createdAt = entity.getCreatedAt();
        this.lastModifiedAt = entity.getLastModifiedAt();
        this.viewCount = entity.getViewCount();
        this.hasImage = !entity.getArticleImages().isEmpty();
        this.commentCount = commentCount;
    }

    public ArticleListResponseDto(Article entity) {
        this(entity, 0);
    }
}
