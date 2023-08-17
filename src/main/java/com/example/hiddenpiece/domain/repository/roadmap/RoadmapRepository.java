package com.example.hiddenpiece.domain.repository.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
}
