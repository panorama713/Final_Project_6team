package com.example.hiddenpiece.domain.repository.roadmap;

import com.example.hiddenpiece.domain.entity.roadmap.RoadmapElement;
import com.example.hiddenpiece.domain.entity.roadmap.RoadmapTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoadmapTodoRepository extends JpaRepository<RoadmapTodo, Long> {
    List<RoadmapTodo> findAllByRoadmapElement(RoadmapElement roadmapElement);

    @Query("SELECT COUNT(rt) FROM RoadmapTodo rt WHERE rt.roadmapElement.id = :elementId")
    long getTotalDoneCountByElementId(@Param("elementId") Long elementId);

    @Query("SELECT COUNT(rt) FROM RoadmapTodo rt WHERE rt.roadmapElement.id = :elementId AND rt.done = true")
    long getTrueDoneCountByElementId(@Param("elementId") Long elementId);
}
