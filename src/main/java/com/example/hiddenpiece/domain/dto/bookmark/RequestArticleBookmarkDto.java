package com.example.hiddenpiece.domain.dto.bookmark;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestArticleBookmarkDto {
    private String title;
}
