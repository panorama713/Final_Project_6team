package com.example.hiddenpiece.domain.repository.roadmap;


import com.example.hiddenpiece.domain.entity.roadmap.RoadmapElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapElementRepository extends JpaRepository<RoadmapElement, Long> {
    List<RoadmapElement> readByRoadmapId(Long roadmapId);
}
