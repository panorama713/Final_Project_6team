package com.example.hiddenpiece.domain.entity.community;
import com.example.hiddenpiece.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "article")
public class Article {

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "boardId")
//    private Board board;

    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;
    //private Long imageId;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private ArticleType type;

    private Long viewCount;

    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime modifiedDate;

    @Builder
    public Article(User user, Category category, String title, String content, ArticleType type) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
        this.modifiedDate = LocalDateTime.now();
    }
}
