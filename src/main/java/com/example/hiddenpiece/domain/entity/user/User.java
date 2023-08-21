package com.example.hiddenpiece.domain.entity.user;

import com.example.hiddenpiece.domain.entity.BaseTimeEntity;
import com.example.hiddenpiece.domain.entity.community.Article;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deletedAt = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at is null")
@Table(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

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
    @OneToMany(mappedBy = "user")
    List<Article> article = new ArrayList<>();

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
}
