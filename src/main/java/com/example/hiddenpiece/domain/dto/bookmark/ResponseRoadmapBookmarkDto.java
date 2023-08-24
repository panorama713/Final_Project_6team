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
    private String title;
    private String username;

    public static ResponseRoadmapBookmarkDto fromEntity(RoadmapBookmark entity) {
        return ResponseRoadmapBookmarkDto.builder()
                .title(entity.getTitle())
                .username(entity.getRoadmap().getUser().getUsername())
                .build();
    }

    public static ResponseRoadmapBookmarkDto fromEntityDelete(RoadmapBookmark entity) {
        return ResponseRoadmapBookmarkDto.builder()
                .title(entity.getTitle() + " - 북마크 취소")
                .username(entity.getRoadmap().getUser().getUsername())
                .build();
    }
}
