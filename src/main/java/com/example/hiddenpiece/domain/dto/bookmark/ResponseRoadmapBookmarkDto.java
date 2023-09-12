package com.example.hiddenpiece.domain.dto.bookmark;

import com.example.hiddenpiece.domain.entity.bookmark.RoadmapBookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRoadmapBookmarkDto {
    private Long roadmapId;
    private String titleOfBookmark;
    private String titleOfRoadmap;
    private String username;
    private String typeOfRoadmap;
    private LocalDateTime createdAt;
    private Long id;

    public static ResponseRoadmapBookmarkDto fromEntity(RoadmapBookmark roadmapBookmark) {
        return ResponseRoadmapBookmarkDto.builder()
                .roadmapId(roadmapBookmark.getRoadmap().getId())
                .titleOfBookmark(roadmapBookmark.getTitle())
                .titleOfRoadmap(roadmapBookmark.getRoadmap().getTitle())
                .username(roadmapBookmark.getRoadmap().getUser().getUsername())
                .typeOfRoadmap(roadmapBookmark.getRoadmap().getType())
                .createdAt(roadmapBookmark.getCreatedAt())
                .id(roadmapBookmark.getId())
                .build();
    }

}
