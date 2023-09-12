package com.example.hiddenpiece.controller.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapTodoCreateDto;
import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapTodoUpdateDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseCreateRoadmapTodoDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseReadRoadmapTodoDto;
import com.example.hiddenpiece.service.roadmap.RoadmapTodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/roadmaps/{roadmapId}/elements/{elementId}/todo")
public class RoadmapTodoController {
    private final RoadmapTodoService roadmapTodoService;

    // create
    // 로드맵 투두 생성
    @PostMapping
    public ResponseEntity<ResponseCreateRoadmapTodoDto> createRoadmapTodo(
            @PathVariable("roadmapId") Long roadmapId,
            @PathVariable("elementId") Long elementId,
            @RequestBody RequestRoadmapTodoCreateDto dto
    ) {
        ResponseCreateRoadmapTodoDto responseDto = roadmapTodoService.create(roadmapId, elementId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    // readALl
    // 로드맵 요소의 투두 목록 조회
    @GetMapping
    public ResponseEntity<List<ResponseReadRoadmapTodoDto>> readRoadmapTodo(
            @PathVariable("roadmapId") Long roadmapId,
            @PathVariable("elementId") Long elementId
    ) {
        List<ResponseReadRoadmapTodoDto> responseDto = roadmapTodoService.read(roadmapId, elementId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    // update
    // 로드맵 투두 수정
    @PutMapping("/{todoId}")
    public ResponseEntity<Void> updateRoadmapTodo(
            @PathVariable("roadmapId") Long roadmapId,
            @PathVariable("elementId") Long elementId,
            @PathVariable("todoId") Long todoId,
            @RequestBody RequestRoadmapTodoUpdateDto dto
    ) {
        roadmapTodoService.update(roadmapId, elementId, todoId, dto);
        return ResponseEntity.noContent().build();
    }

    // 로드맵 투두 done/undone
    @PutMapping("/{todoId}/done")
    public ResponseEntity<Void> checkDone(
            @PathVariable("roadmapId") Long roadmapId,
            @PathVariable("elementId") Long elementId,
            @PathVariable("todoId") Long todoId
    ) {
        roadmapTodoService.checkDone(roadmapId, elementId, todoId);
        return ResponseEntity.noContent().build();
    }

    // delete
    // 로드맵 투두 삭제
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteRoadmapTodo(
            @PathVariable("roadmapId") Long roadmapId,
            @PathVariable("elementId") Long elementId,
            @PathVariable("todoId") Long todoId
    ) {
        roadmapTodoService.delete(roadmapId, elementId, todoId);
        return ResponseEntity.noContent().build();
    }

    // 로드맵 투두 달성율
    @GetMapping("/done-progress")
    public Long doneProgress(
            @PathVariable("elementId") Long elementId
    ) {
        return roadmapTodoService.todoProgress(elementId);
    }
}
