package com.example.hiddenpiece.service.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapElementDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseRoadmapElementDto;
import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.roadmap.RoadmapCategory;
import com.example.hiddenpiece.domain.entity.roadmap.RoadmapElement;
import com.example.hiddenpiece.domain.repository.roadmap.RoadmapCategoryRepository;
import com.example.hiddenpiece.domain.repository.roadmap.RoadmapElementRepository;
import com.example.hiddenpiece.domain.repository.roadmap.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoadmapElementService {
    private final RoadmapRepository roadmapRepository;
    private final RoadmapCategoryRepository roadmapCategoryRepository;
    private final RoadmapElementRepository roadmapElementRepository;
    // todo: user repository

    // 로드맵 요소 추가 기능
    // create
    public ResponseRoadmapElementDto createRoadmapElement(RequestRoadmapElementDto dto, Long roadmapId, Long roadmapCategoryId) {
        // Roadmap 존재 확인
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new RuntimeException("로드맵이 존재하지 않습니다."));

        // Roadmap_category 확인
        RoadmapCategory roadmapCategory = roadmapCategoryRepository.findById(roadmapCategoryId)
                .orElseThrow(() -> new RuntimeException("로드맵 카테고리가 존재하지 않습니다."));

        // Roadmap 이 존재한다면
        RoadmapElement entity = RoadmapElement.builder()
                .roadmap(roadmap)
                .roadmapCategory(roadmapCategory)
                .title(dto.getTitle())
                .content(dto.getContent())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        log.info("roadmapElement: {}", entity);

        return ResponseRoadmapElementDto.fromEntity(roadmapElementRepository.save(entity));
    }
}
