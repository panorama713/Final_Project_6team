package com.example.hiddenpiece.domain.entity.bookmark;

import com.example.hiddenpiece.domain.dto.bookmark.RequestRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Setter
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @Setter
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
