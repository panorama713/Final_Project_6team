package com.example.hiddenpiece.controller.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapTodoDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseRoadmapTodoDto;
import com.example.hiddenpiece.domain.repository.roadmap.RoadmapTodoRepository;
import com.example.hiddenpiece.service.roadmap.RoadmapTodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/roadmaps/elements/{elementId}/todo")
public class RoadmapTodoController {
    private final RoadmapTodoService roadmapTodoService;

    // create
    // 로드맵 투두 생성
    @PostMapping
    public ResponseEntity<ResponseRoadmapTodoDto> createRoadmapTodo(
            @PathVariable("elementId") Long elementId,
            @RequestBody RequestRoadmapTodoDto dto
    ) {
        ResponseRoadmapTodoDto responseDto = roadmapTodoService.create(elementId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    // readALl
    // 로드맵 요소의 투두 목록 조회

    // update
    // 로드맵 투두 수정

    // delete
    // 로드맵 투두 삭제

}
