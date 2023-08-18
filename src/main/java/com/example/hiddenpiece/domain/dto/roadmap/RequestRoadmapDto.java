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
public class RequestRoadmapDto {
    private String title;
    private String type;
    private String description;

    public static RequestRoadmapDto fromEntity(Roadmap entity) {
        return RequestRoadmapDto.builder()
                .title(entity.getTitle())
                .type(entity.getType())
                .description(entity.getDescription())
                .build();
    }
}
