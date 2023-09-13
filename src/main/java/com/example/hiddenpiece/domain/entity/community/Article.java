package com.example.hiddenpiece.domain.entity.community;

import com.example.hiddenpiece.domain.entity.BaseTimeEntity;
import com.example.hiddenpiece.domain.entity.bookmark.ArticleBookmark;
import com.example.hiddenpiece.domain.entity.image.ArticleImage;
import com.example.hiddenpiece.domain.entity.like.Like;
import com.example.hiddenpiece.domain.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE article SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at is null")
public class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ArticleImage> articleImages = new ArrayList<>();

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private ArticleType type;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int viewCount;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int likeCount;

    private LocalDateTime deletedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Like> likeArticles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ArticleBookmark> articleBookmarks = new ArrayList<>();

    @Builder
    public Article(
            User user, Category category, String title,
            String content, ArticleType type, Long viewCount
    ) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.type = type;
        this.viewCount = 0;
    }

    public void modify(String title, String content, ArticleType type, Category category) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.category = category;
    }

    public void addLikeArticles(Like like) {
        if (!likeArticles.contains(like)) likeArticles.add(like);
    }

    public void addArticleBookmarks(ArticleBookmark articleBookmark) {
        articleBookmark.setArticle(this);
        this.articleBookmarks.add(articleBookmark);
    }
}
