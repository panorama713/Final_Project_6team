package com.example.hiddenpiece.domain.repository.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.ResponseSearchRoadmapDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseTop5RoadmapDto;
import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    @Query("SELECT r FROM Roadmap r WHERE r.user = :user " +
            "AND (:targetDate IS NULL OR r.createdAt >= :targetDate) " +
            "AND (:targetYear IS NULL OR r.createdAt <= :targetYear) " +
            "AND (:type IS NULL OR r.type = :type)")
    List<Roadmap> findRoadmapByUserAndCreatedAtAndType(
            @Param("user") User user,
            @Param("targetDate") LocalDateTime targetDate,
            @Param("targetYear") LocalDateTime targetYear,
            @Param("type") String type
    );

    @Query("SELECT COUNT(r) FROM Roadmap r WHERE r.createdAt = CURRENT_DATE")
    Integer countTodayRoadmaps();

    @Query("SELECT new com.example.hiddenpiece.domain.dto.roadmap.ResponseTop5RoadmapDto(r.title, r.user.username) " +
            "FROM Roadmap r " +
            "GROUP BY r.title, r.user.username " +
            "ORDER BY r.id DESC LIMIT 5")
    List<ResponseTop5RoadmapDto> findTop5ByRoadmapsWithId();

    @Query("SELECT new com.example.hiddenpiece.domain.dto.roadmap.ResponseTop5RoadmapDto(r.title, r.user.username) " +
            "FROM Roadmap r " +
            "GROUP BY r.title, r.user.username " +
            "ORDER BY RANDOM() LIMIT 5")
    List<ResponseTop5RoadmapDto> findTop5ByRoadmapsWithRandom();

    @Query("SELECT new com.example.hiddenpiece.domain.dto.roadmap.ResponseSearchRoadmapDto(r.id, r.title, r.description, r.user.username) " +
           "FROM Roadmap r " +
           "WHERE r.title LIKE %:keyword% OR r.description LIKE %:keyword% " +
           "ORDER BY r.id")
    Page<ResponseSearchRoadmapDto> findByContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new com.example.hiddenpiece.domain.dto.roadmap.ResponseSearchRoadmapDto(r.id, r.title, r.description, r.user.username) " +
            "FROM Roadmap r " +
            "WHERE (:field IS NULL OR r.type = :field) AND (:keyword IS NULL OR r.title LIKE %:keyword% OR r.description LIKE %:keyword%)")
    Page<ResponseSearchRoadmapDto> findRoadmapByTypeOrderBySort(
            @Param("keyword") String keyword, @Param("field") String field, Pageable pageable);
}
