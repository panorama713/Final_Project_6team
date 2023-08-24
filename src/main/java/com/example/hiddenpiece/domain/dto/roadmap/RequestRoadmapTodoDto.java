package com.example.hiddenpiece.domain.dto.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.roadmap.RoadmapTodo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestRoadmapTodoDto {
    private String title;
    private String content;
    private String url;
    private Boolean done;
}
