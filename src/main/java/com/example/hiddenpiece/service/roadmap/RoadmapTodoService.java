package com.example.hiddenpiece.service.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestCreateRoadmapTodoDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseCreateRoadmapTodoDto;
import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.roadmap.RoadmapElement;
import com.example.hiddenpiece.domain.entity.roadmap.RoadmapTodo;
import com.example.hiddenpiece.domain.repository.roadmap.RoadmapElementRepository;
import com.example.hiddenpiece.domain.repository.roadmap.RoadmapRepository;
import com.example.hiddenpiece.domain.repository.roadmap.RoadmapTodoRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoadmapTodoService {
    private final RoadmapTodoRepository roadmapTodoRepository;
    private final RoadmapRepository roadmapRepository;
    private final RoadmapElementRepository roadmapElementRepository;

    // 로드맵 투두 생성 기능
    @Transactional
    public ResponseCreateRoadmapTodoDto create(Long roadmapId, Long elementId, RequestCreateRoadmapTodoDto dto) {
        Roadmap targetRoadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP));

        RoadmapElement targetRoadmapElement = roadmapElementRepository.findById(elementId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP_ELEMENT));

        RoadmapTodo newRoadmapTodo = RoadmapTodo.builder()
                .roadmapElement(targetRoadmapElement)
                .title(dto.getTitle())
                .content(dto.getContent())
                .url(dto.getUrl())
                .build();

        roadmapTodoRepository.save(newRoadmapTodo);
        log.info("로드맵 Todo 생성 성공");

        return ResponseCreateRoadmapTodoDto.fromEntity(newRoadmapTodo);
    }
}
