package com.example.hiddenpiece.domain.dto.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseFollowingRoadmapsDto {
    private Long roadmapId;
    private String writer;
    private String title;
    private String description;
    private LocalDateTime createdAt;

    @Builder
    public ResponseFollowingRoadmapsDto(Long roadmapId, String writer, String title, String description, LocalDateTime createdAt) {
        this.roadmapId = roadmapId;
        this.writer = writer;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static ResponseFollowingRoadmapsDto fromEntity(Roadmap roadmap) {
        return ResponseFollowingRoadmapsDto.builder()
                .roadmapId(roadmap.getId())
                .writer(roadmap.getUser().getUsername())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .createdAt(roadmap.getCreatedAt())
                .build();
    }
}
