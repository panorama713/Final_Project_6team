package com.example.hiddenpiece.domain.dto.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.RoadmapElement;
import com.example.hiddenpiece.domain.entity.roadmap.RoadmapTodo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseRoadmapTodoDto {
    private String title;
    private String content;
    private String url;
    private Boolean done;

    public static ResponseRoadmapTodoDto fromEntity(RoadmapTodo entity) {
        return ResponseRoadmapTodoDto.builder()
                .title(entity.getTitle())
                .content(entity.getContent())
                .url(entity.getUrl())
                .done(entity.getDone())
                .build();
    }
}
