package com.example.hiddenpiece.domain.entity.bookmark;

import com.example.hiddenpiece.domain.entity.BaseTimeEntity;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "article_bookmark")
public class ArticleBookmark extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @Setter
    private Article article;

    private String title;

    @Builder
    public ArticleBookmark(User user, Article article, String title) {
        this.user = user;
        this.article = article;
        this.title = title;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
