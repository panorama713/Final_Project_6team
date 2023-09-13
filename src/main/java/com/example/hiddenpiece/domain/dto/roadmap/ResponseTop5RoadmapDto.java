package com.example.hiddenpiece.domain.dto.roadmap;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseTop5RoadmapDto {
    private Long roadmapId;
    private String title;
    private String username;

    @Builder
    public ResponseTop5RoadmapDto(Long roadmapId, String title, String username) {
        this.roadmapId = roadmapId;
        this.title = title;
        this.username = username;
    }
}
