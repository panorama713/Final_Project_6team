package com.example.hiddenpiece.domain.repository.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.RoadmapElement;
import com.example.hiddenpiece.domain.entity.roadmap.RoadmapTodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapTodoRepository extends JpaRepository<RoadmapTodo, Long> {
    List<RoadmapTodo> findAllByRoadmapElement(RoadmapElement roadmapElement);
}
