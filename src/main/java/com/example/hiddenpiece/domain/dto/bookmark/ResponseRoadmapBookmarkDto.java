package com.example.hiddenpiece.domain.dto.bookmark;

import com.example.hiddenpiece.domain.entity.bookmark.RoadmapBookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRoadmapBookmarkDto {
    private String titleOfBookmark;
    private String titleOfArticle;
    private String username;

    public static ResponseRoadmapBookmarkDto fromEntity(RoadmapBookmark roadmapBookmark) {
        return ResponseRoadmapBookmarkDto.builder()
                .titleOfBookmark(roadmapBookmark.getTitle())
                .titleOfArticle(roadmapBookmark.getRoadmap().getTitle())
                .username(roadmapBookmark.getUser().getUsername())
                .build();
    }
}
