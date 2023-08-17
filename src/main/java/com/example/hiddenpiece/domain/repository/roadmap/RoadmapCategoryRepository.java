package com.example.hiddenpiece.domain.repository.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.RoadmapCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapCategoryRepository extends JpaRepository<RoadmapCategory, Long> {
}
