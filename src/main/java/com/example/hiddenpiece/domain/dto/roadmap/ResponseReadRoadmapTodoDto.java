package com.example.hiddenpiece.domain.dto.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.RoadmapTodo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseReadRoadmapTodoDto {
    private String title;
    private String content;
    private String url;
    private Boolean done;

    public static ResponseReadRoadmapTodoDto fromEntity(RoadmapTodo entity) {
        return ResponseReadRoadmapTodoDto.builder()
                .title(entity.getTitle())
                .content(entity.getContent())
                .url(entity.getUrl())
                .done(entity.getDone())
                .build();
    }
}
