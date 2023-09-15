package com.example.hiddenpiece.domain.dto.bookmark;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestRoadmapBookmarkDto {
    private String title;
}
