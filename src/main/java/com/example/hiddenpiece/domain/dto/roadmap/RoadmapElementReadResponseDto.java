package com.example.hiddenpiece.domain.dto.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.RoadmapElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapElementReadResponseDto {
    private String title;

    public static RoadmapElementReadResponseDto fromEntity(RoadmapElement entity) {
        return RoadmapElementReadResponseDto.builder()
                .title(entity.getTitle())
                .build();
    }
}
