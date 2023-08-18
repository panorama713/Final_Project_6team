package com.example.hiddenpiece.domain.dto.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRoadmapDto {
    private String title;
    private User user;
    private String type;
    private String description;

    public static ResponseRoadmapDto fromEntity(Roadmap entity) {
        return ResponseRoadmapDto.builder()
                .title(entity.getTitle())
                .user(entity.getUser())
                .type(entity.getType())
                .description(entity.getDescription())
                .build();
    }
}
