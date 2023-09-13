package com.example.hiddenpiece.domain.dto.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.RoadmapElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapElementReadResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static RoadmapElementReadResponseDto fromEntity(RoadmapElement entity) {
        return RoadmapElementReadResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }
}
