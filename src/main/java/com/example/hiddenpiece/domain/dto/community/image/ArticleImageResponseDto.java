package com.example.hiddenpiece.domain.dto.community.image;

import com.example.hiddenpiece.domain.entity.image.ArticleImage;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArticleImageResponseDto {
    private Long id;
    private String imageName;
    private String imageUrl;
    private Long imageSize;

    public static ArticleImageResponseDto fromEntity(ArticleImage image) {
        return ArticleImageResponseDto.builder()
                .id(image.getId())
                .imageName(image.getImageName())
                .imageUrl(image.getImageUrl())
                .imageSize(image.getImageSize())
                .build();
    }
}
