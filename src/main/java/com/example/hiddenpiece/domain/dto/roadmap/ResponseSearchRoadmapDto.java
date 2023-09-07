package com.example.hiddenpiece.domain.dto.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseSearchRoadmapDto {
    private Long roadmapId;
    private String title;
    private String description;
    private String writer;

    public ResponseSearchRoadmapDto(Long roadmapId, String title, String description, String writer) {
        this.roadmapId = roadmapId;
        this.title = title;
        this.description = description;
        this.writer = writer;
    }
}
