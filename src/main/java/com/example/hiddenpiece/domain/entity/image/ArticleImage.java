package com.example.hiddenpiece.domain.entity.image;

import com.example.hiddenpiece.domain.entity.community.Article;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    private String imageName;
    private String imageUrl;
    private Long imageSize;

    @Builder
    public ArticleImage(Article article, String imageName, String imageUrl, Long imageSize) {
        this.article = article;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.imageSize = imageSize;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
