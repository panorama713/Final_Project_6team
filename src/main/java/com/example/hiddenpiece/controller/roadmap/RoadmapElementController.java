package com.example.hiddenpiece.controller.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapElementDto;
import com.example.hiddenpiece.domain.dto.roadmap.RoadmapElementReadResponseDto;
import com.example.hiddenpiece.common.ResponseDto;
import com.example.hiddenpiece.common.SystemMessage;
import com.example.hiddenpiece.service.roadmap.RoadmapElementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/roadmaps/{roadmapId}")
public class RoadmapElementController {
    private final RoadmapElementService roadmapElementService;

    // post
    // 로드맵 요소 생성
    @PostMapping("/elements/{roadmapCategoryId}")
    public ResponseEntity<ResponseDto> createRoadmapElement(
            @Validated @RequestBody RequestRoadmapElementDto dto,
            @PathVariable("roadmapId") Long roadmapId,
            @PathVariable("roadmapCategoryId") Long roadmapCategoryId
    ) {
        roadmapElementService.createRoadmapElement(dto, roadmapId, roadmapCategoryId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDto.getInstance(SystemMessage.CREATED_ELEMENT));
    }

    // get
    // 로드맵 요소 목록 조회
    @GetMapping("/elements/{roadmapCategoryId}")
    public ResponseEntity<List<RoadmapElementReadResponseDto>> readAllElementList(
            @PathVariable("roadmapId") Long roadmapId,
            @PathVariable("roadmapCategoryId") Long roadmapCategoryId
    ) {
        List<RoadmapElementReadResponseDto> dtoList = roadmapElementService.readAllRoadmapElementList(roadmapId, roadmapCategoryId);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
