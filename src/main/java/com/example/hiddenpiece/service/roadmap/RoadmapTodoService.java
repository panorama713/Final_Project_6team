package com.example.hiddenpiece.service.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapTodoCreateDto;
import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapTodoUpdateDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseCreateRoadmapTodoDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseReadRoadmapTodoDto;
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

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoadmapTodoService {
    private final RoadmapTodoRepository roadmapTodoRepository;
    private final RoadmapRepository roadmapRepository;
    private final RoadmapElementRepository roadmapElementRepository;

    // 로드맵 투두 생성
    @Transactional
    public ResponseCreateRoadmapTodoDto create(Long roadmapId, Long elementId, RequestRoadmapTodoCreateDto dto) {
        if (!roadmapRepository.existsById(roadmapId))
            throw new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP);

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

    // 로드맵 요소별 투두 목록 조회
    public List<ResponseReadRoadmapTodoDto> read(Long roadmapId, Long elementId) {
        if (!roadmapRepository.existsById(roadmapId))
            throw new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP);

        RoadmapElement targetRoadmapElement = roadmapElementRepository.findById(elementId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP_ELEMENT));

        return roadmapTodoRepository.findAllByRoadmapElement(targetRoadmapElement)
                .stream()
                .map(ResponseReadRoadmapTodoDto::fromEntity)
                .toList();
    }


    // 로드맵 투두 수정
    @Transactional
    public void update(Long roadmapId, Long elementId, Long todoId, RequestRoadmapTodoUpdateDto dto) {
        if (!roadmapRepository.existsById(roadmapId))
            throw new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP);
        if (!roadmapElementRepository.existsById(elementId))
            throw new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP_ELEMENT);
        RoadmapTodo targetRoadmapTodo = roadmapTodoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP_TODO));

        targetRoadmapTodo.update(dto);
        roadmapTodoRepository.save(targetRoadmapTodo);
        log.info("로드맵 Todo 수정 성공");
    }

    @Transactional
    public void checkDone(Long roadmapId, Long elementId, Long todoId) {
        if (!roadmapRepository.existsById(roadmapId))
            throw new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP);
        if (!roadmapElementRepository.existsById(elementId))
            throw new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP_ELEMENT);
        RoadmapTodo targetRoadmapTodo = roadmapTodoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP_TODO));

        targetRoadmapTodo.checkDone();
        roadmapTodoRepository.save(targetRoadmapTodo);

        if (targetRoadmapTodo.getDone()) {
            log.info("{} Done!!", targetRoadmapTodo.getTitle());
        } else {
            log.info("{} Undone!!", targetRoadmapTodo.getTitle());
        }
    }

    // 로드맵 투두 삭제
    @Transactional
    public void delete(Long roadmapId, Long elementId, Long todoId) {
        if (!roadmapRepository.existsById(roadmapId))
            throw new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP);
        if (!roadmapElementRepository.existsById(elementId))
            throw new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP_ELEMENT);
        RoadmapTodo targetRoadmapTodo = roadmapTodoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP_TODO));

        roadmapTodoRepository.delete(targetRoadmapTodo);
        log.info("로드맵 Todo 삭제 성공");
    }
}
