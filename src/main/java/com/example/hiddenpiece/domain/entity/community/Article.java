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
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE article SET deleted_at = CURRENT_TIMESTAMP where id = ?")
@Table(name = "article")
public class Article extends BaseTimeEntity {

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

//    private LocalDateTime createdDate = LocalDateTime.now();
//    private LocalDateTime modifiedDate;

    @Column(name = "deleted_at")
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
