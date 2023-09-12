package com.example.hiddenpiece.domain.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseFollowingDto {
    private Long toUserId;
    private String username;
    private LocalDateTime createdAt;

    @Builder
    public ResponseFollowingDto(Long toUserId, String username, LocalDateTime createdAt) {
        this.toUserId = toUserId;
        this.username = username;
        this.createdAt = createdAt;
    }
}
