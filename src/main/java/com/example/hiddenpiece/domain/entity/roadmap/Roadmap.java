package com.example.hiddenpiece.domain.entity.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapDto;
import com.example.hiddenpiece.domain.entity.BaseTimeEntity;
import com.example.hiddenpiece.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "roadmaps")
public class Roadmap extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String type;
    private String title;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Builder
    public Roadmap(Long id, User user, String type, String title, String description, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public void update(RequestRoadmapDto dto) {
        this.type = dto.getType();
        this.title = dto.getTitle();
        this.description = dto.getTitle();
        this.updatedAt = LocalDateTime.now();
    }
}
