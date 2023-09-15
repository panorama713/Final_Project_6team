package com.example.hiddenpiece.domain.dto.user;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserProfileResponseDto {
    private Long userId;
    private String username;
    private String realName;
    private String email;
    private String profileImg;
    private int numberOfWrittenArticle;     // TODO 기능 구현시 구현 예정
    private int numberOfWrittenComment;     // TODO 기능 구현시 구현 예정
    private int followerCount;
    private int followingCount;
}