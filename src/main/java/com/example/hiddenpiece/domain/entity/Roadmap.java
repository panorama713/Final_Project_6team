package com.example.hiddenpiece.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "roadmap")
public class Roadmap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String type;
    private String title;
    private String description;

    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;

    @Builder
    public Roadmap(Long id, User user, String type, String title, String description, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.title = title;
        this.description = description;
    }
}
