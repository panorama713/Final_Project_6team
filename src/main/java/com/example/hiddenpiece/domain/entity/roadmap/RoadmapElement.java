package com.example.hiddenpiece.domain.entity.roadmap;

import com.example.hiddenpiece.domain.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "roadmap_elements")
@SQLDelete(sql = "UPDATE roadmap_elements SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at is null")
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

    private String title;
    private String content;
    private boolean done;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalDateTime deleted_at;

    @Builder
    public RoadmapElement(Roadmap roadmap, String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
        this.roadmap = roadmap;
        this.title = title;
        this.content = content;
        // 기본으로 체크가 되어있지 않음
        this.done = false;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
