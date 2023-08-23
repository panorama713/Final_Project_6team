package com.example.hiddenpiece.domain.entity.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "roadmap_bookmark")
public class RoadmapBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "roadmap_id")
    Roadmap roadmap;

    @Builder
    public RoadmapBookmark(Long id, String title, User user, Roadmap roadmap) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.roadmap = roadmap;
    }

    public void update(RequestRoadmapBookmarkDto dto) {
        this.title = dto.getTitle();
    }
}
