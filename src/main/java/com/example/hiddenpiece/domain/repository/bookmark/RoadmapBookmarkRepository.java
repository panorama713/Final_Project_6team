package com.example.hiddenpiece.domain.repository.bookmark;

import com.example.hiddenpiece.domain.dto.roadmap.ResponseTop5RoadmapDto;
import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.bookmark.RoadmapBookmark;
import com.example.hiddenpiece.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoadmapBookmarkRepository extends JpaRepository<RoadmapBookmark, Long> {
    boolean existsByUserAndRoadmap(User user, Roadmap roadmap);

    Page<RoadmapBookmark> findAllByUser(User user, Pageable pageable);

    @Query("SELECT new com.example.hiddenpiece.domain.dto.roadmap.ResponseTop5RoadmapDto(rb.roadmap.id, rb.roadmap.title, rb.roadmap.user.username) " +
           "FROM RoadmapBookmark rb " +
           "GROUP BY rb.roadmap.id, rb.roadmap.title, rb.roadmap.user.username " +
           "ORDER BY COUNT(rb) DESC LIMIT 5")
    List<ResponseTop5RoadmapDto> findTop5ByRoadmapsWithBookmarkCount();

    RoadmapBookmark findByUserAndRoadmap(User user, Roadmap roadmap);

}
