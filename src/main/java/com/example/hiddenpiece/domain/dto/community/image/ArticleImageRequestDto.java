package com.example.hiddenpiece.domain.dto.community.image;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArticleImageRequestDto {
    private String imageName;
    private String imageUrl;
    private Long imageSize;
}
