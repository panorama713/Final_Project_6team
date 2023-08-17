package com.example.hiddenpiece.domain.dto.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.RoadmapElement;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseRoadmapElementDto {
    private Long id;
    private Long userId;
    private Long roadmapId;
    private Long roadmapCategory;
    private String title;
    private String content;
    private boolean done;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static ResponseRoadmapElementDto fromEntity(RoadmapElement roadmapElement) {
        ResponseRoadmapElementDto dto = new ResponseRoadmapElementDto();

        dto.setId(roadmapElement.getId());
//        dto.setUserId(roadmapElement.getUser().getId());
        dto.setRoadmapId(roadmapElement.getRoadmap().getId());
        dto.setRoadmapCategory(roadmapElement.getRoadmapCategory().getId());
        dto.setTitle(roadmapElement.getTitle());
        dto.setContent(roadmapElement.getContent());
        dto.setDone(roadmapElement.isDone());
        dto.setStartDate(roadmapElement.getStartDate());
        dto.setEndDate(roadmapElement.getEndDate());
        return dto;
    }
}
