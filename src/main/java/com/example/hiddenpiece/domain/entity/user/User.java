package com.example.hiddenpiece.domain.entity.user;

import com.example.hiddenpiece.domain.entity.BaseTimeEntity;
import com.example.hiddenpiece.domain.entity.bookmark.ArticleBookmark;
import com.example.hiddenpiece.domain.entity.bookmark.RoadmapBookmark;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.like.Like;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at is null")
@Table(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    private String password;
    private String realName;

    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImg;

    private String provider;
    private String providerId;

    // 인증 방식 미정
    private String question;
    private String answer;
    private LocalDateTime deletedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Like> likeArticles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<RoadmapBookmark> roadmapBookmarks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ArticleBookmark> articleBookmarks = new ArrayList<>();

    @Builder
    public User(
            String username, String password, String realName,
            String email, String phone, Role role,
            String profileImg, String provider, String providerId
    ) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.profileImg = profileImg;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updateInfo(String newPassword, String newEmail, String newPhone, String imagePath) {
        this.password = newPassword;
        this.email = newEmail;
        this.phone = newPhone;
        this.profileImg = imagePath;
    }

    public void addLikeArticles(Like like) {
        if (!likeArticles.contains(like)) likeArticles.add(like);
    }

    public void addRoadmapBookmarks(RoadmapBookmark roadmapBookmark) {
        roadmapBookmark.setUser(this);
        this.roadmapBookmarks.add(roadmapBookmark);
    }

    public void addArticleBookmarks(ArticleBookmark articleBookmark) {
        articleBookmark.setUser(this);
        this.articleBookmarks.add(articleBookmark);
    }
}
