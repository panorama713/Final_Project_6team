package com.example.hiddenpiece.domain.dto.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRoadmapDto {
    private String title;
    private String username;
    private String type;
    private String description;

    public static ResponseRoadmapDto fromEntity(Roadmap entity) {
        return ResponseRoadmapDto.builder()
                .title(entity.getTitle())
                .username(entity.getUser().getUsername())
                .type(entity.getType())
                .description(entity.getDescription())
                .build();
    }
}
