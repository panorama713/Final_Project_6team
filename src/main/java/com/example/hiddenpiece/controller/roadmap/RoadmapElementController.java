package com.example.hiddenpiece.controller.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapElementDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseRoadmapElementDto;
import com.example.hiddenpiece.service.roadmap.RoadmapElementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/roadmaps/{roadmapId}")
public class RoadmapElementController {
    private final RoadmapElementService roadmapElementService;

    // post
    // 로드맵 요소 생성
    @PostMapping("/elements/{roadmapCategoryId}")
    public ResponseRoadmapElementDto createRoadmapElement(
            @Validated @RequestBody RequestRoadmapElementDto dto,
            @PathVariable("roadmapId") Long roadmapId,
            @PathVariable("roadmapCategoryId") Long roadmapCategoryId
    ) {
        ResponseRoadmapElementDto response = roadmapElementService.createRoadmapElement(
                dto, roadmapId, roadmapCategoryId
        );
        return response;
    }
}
