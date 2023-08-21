package com.example.hiddenpiece.domain.repository.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.ResponseRoadmapDto;
import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.user.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    List<Roadmap> findByUser(User user);
//    @Query("SELECT r FROM Roadmap r WHERE r.user = :user AND YEAR(r.createdAt) = YEAR(:targetDate) AND r.createdAt >= :targetDate")
//    List<Roadmap> findRoadmapByUserAndCreatedAt(@Param("user") User user, @Param("targetDate") LocalDateTime targetDate);

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

}
