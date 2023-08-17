package com.example.hiddenpiece.domain.entity.roadmap;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "roadmap_categorys")
public class RoadmapCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Builder
    public RoadmapCategory(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
