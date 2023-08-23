package com.example.hiddenpiece.domain.entity.community;
import com.example.hiddenpiece.domain.entity.BaseTimeEntity;
import com.example.hiddenpiece.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE article SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at is null")
public class Article extends BaseTimeEntity {

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "boardId")
//    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private Long imageId;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private ArticleType type;

    private Long viewCount;

    private LocalDateTime deletedAt;

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
    }
}
