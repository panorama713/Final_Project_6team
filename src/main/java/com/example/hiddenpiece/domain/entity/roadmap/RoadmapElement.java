package com.example.hiddenpiece.domain.entity.roadmap;

import com.example.hiddenpiece.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "roadmap_elements")
public class RoadmapElement extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // roadmap 불러올때 이미 User 검증을 마치므로 필요 없을 것 같음
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    @ManyToOne
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap;

    @ManyToOne
    @JoinColumn(name = "roadmapCategory_id")
    private RoadmapCategory roadmapCategory;

    private String title;
    private String content;
    private boolean done;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalDateTime deleted_at;

    @Builder
    public RoadmapElement(Roadmap roadmap, RoadmapCategory roadmapCategory, String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
        this.roadmap = roadmap;
        this.roadmapCategory = roadmapCategory;
        this.title = title;
        this.content = content;
        // 기본으로 체크가 되어있지 않음
        this.done = false;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
