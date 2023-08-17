package com.example.hiddenpiece.domain.entity.roadmap;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "roadmap_categorys")
public class RoadmapCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @OneToMany(mappedBy = "roadmapCategory")
    private List<RoadmapElement> roadmapElementList;

    @Builder
    public RoadmapCategory(Long id, String title) {
        this.id = id;
        this.title = title;
    }


}
