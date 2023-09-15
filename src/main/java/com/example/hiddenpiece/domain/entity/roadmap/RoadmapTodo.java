package com.example.hiddenpiece.domain.entity.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapTodoUpdateDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "roadmap_todo")
public class RoadmapTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_element_id")
    private RoadmapElement roadmapElement;

    private String title;
    private String content;
    private String url;
    private Boolean done;

    @Builder
    public RoadmapTodo(RoadmapElement roadmapElement, String title, String content, String url) {
        this.roadmapElement = roadmapElement;
        this.title = title;
        this.content = content;
        this.url = url;
        // 기본으로 체크가 되어있지 않음
        this.done = false;
    }

    public void update(RequestRoadmapTodoUpdateDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.url = dto.getUrl();
    }

    public void checkDone() {
        if (!this.done)
            this.done = true;
        else
            this.done = false;
    }
}
