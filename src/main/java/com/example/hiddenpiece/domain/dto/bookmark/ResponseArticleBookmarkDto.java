package com.example.hiddenpiece.domain.dto.bookmark;

import com.example.hiddenpiece.domain.entity.bookmark.ArticleBookmark;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResponseArticleBookmarkDto {
    private String titleOfBookmark;
    private String usernameOfArticle;

    public static ResponseArticleBookmarkDto fromEntity(ArticleBookmark articleBookmark) {
        return ResponseArticleBookmarkDto.builder()
                .titleOfBookmark(articleBookmark.getTitle())
                .usernameOfArticle(articleBookmark.getArticle().getUser().getUsername())
                .build();
    }
}
