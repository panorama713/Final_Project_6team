package com.example.hiddenpiece.domain.repository.roadmap;


import com.example.hiddenpiece.domain.entity.roadmap.RoadmapElement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapElementRepository extends JpaRepository<RoadmapElement, Long> {
}
