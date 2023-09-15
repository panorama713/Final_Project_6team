
package com.example.hiddenpiece.domain.dto.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseMyPageRoadmapDto {
    private Long id;
    private String type;
    private String title;
    private String description;
    private LocalDateTime createdAt;

    @Builder
    public ResponseMyPageRoadmapDto(Long id, String type, String title, String description, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static ResponseMyPageRoadmapDto fromEntity(Roadmap roadmap) {
        return ResponseMyPageRoadmapDto.builder()
                .id(roadmap.getId())
                .type(roadmap.getType())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .createdAt(roadmap.getCreatedAt())
                .build();
    }
}
