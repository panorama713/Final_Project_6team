package com.example.hiddenpiece.domain.dto.community.article;
import com.example.hiddenpiece.domain.entity.community.ArticleType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArticleResponseDto {
    private Long articleId;
    private String username;
    private String title;
    private String content;
    private ArticleType type;
    private int likeCount;
}
