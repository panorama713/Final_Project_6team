package com.example.hiddenpiece.domain.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRoadmapTodoDoneProgress {
    private Long doneProgress;
}