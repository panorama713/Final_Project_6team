package com.example.hiddenpiece.domain.dto.roadmap;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseTop5RoadmapDto {
    private String title;
    private String username;

    @Builder
    public ResponseTop5RoadmapDto(String title, String username) {
        this.title = title;
        this.username = username;
    }
}
