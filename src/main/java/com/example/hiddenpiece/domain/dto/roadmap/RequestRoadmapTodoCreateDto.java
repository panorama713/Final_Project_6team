package com.example.hiddenpiece.domain.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestRoadmapTodoCreateDto {
    private String title;
    private String content;
    private String url;
}
