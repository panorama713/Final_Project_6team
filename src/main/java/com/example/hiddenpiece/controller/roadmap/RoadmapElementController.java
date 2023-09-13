package com.example.hiddenpiece.controller.roadmap;

import com.example.hiddenpiece.common.ResponseDto;
import com.example.hiddenpiece.common.SystemMessage;
import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapElementDto;
import com.example.hiddenpiece.domain.dto.roadmap.RoadmapElementReadResponseDto;
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
    @PostMapping("/elements")
    public ResponseEntity<ResponseDto> createRoadmapElement(
            @Validated @RequestBody RequestRoadmapElementDto dto,
            @PathVariable("roadmapId") Long roadmapId
    ) {
        roadmapElementService.createRoadmapElement(dto, roadmapId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDto.getInstance(SystemMessage.CREATED_ELEMENT));
    }

    // get
    // 로드맵 요소 목록 조회
    @GetMapping("/elements")
    public ResponseEntity<List<RoadmapElementReadResponseDto>> readAllElementList(
            @PathVariable("roadmapId") Long roadmapId
    ) {
        List<RoadmapElementReadResponseDto> dtoList = roadmapElementService.readAllRoadmapElementList(roadmapId);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    // update
    // 로드맵 요소 수정
    @PutMapping("/elements/{elementsId}")
    public ResponseEntity<ResponseDto> updateRoadmapElement(
            @Validated @RequestBody RequestRoadmapElementDto dto,
            @PathVariable("roadmapId") Long roadmapId,
            @PathVariable("elementsId") Long elementId
    ) {
        roadmapElementService.updateRoadmapElement(dto, roadmapId, elementId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.getInstance(SystemMessage.UPDATE_ELEMENT));
    }

    // delete
    // 로드맵 요소 삭제
    @DeleteMapping("/elements/{elementsId}")
    public ResponseEntity<ResponseDto> deleteRoadmapElement(
            @PathVariable("roadmapId") Long roadmapId,
            @PathVariable("elementsId") Long roadmapElementId
    ) {
        roadmapElementService.deleteRoadmapElement(roadmapId, roadmapElementId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.getInstance(SystemMessage.DELETED_ELEMENT));
    }
}
