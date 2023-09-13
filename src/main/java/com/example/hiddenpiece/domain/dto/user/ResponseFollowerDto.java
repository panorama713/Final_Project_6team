package com.example.hiddenpiece.domain.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseFollowerDto {
    private Long fromUserId;
    private String username;
    private LocalDateTime createdAt;

    @Builder
    public ResponseFollowerDto(Long fromUserId, String username, LocalDateTime createdAt) {
        this.fromUserId = fromUserId;
        this.username = username;
        this.createdAt = createdAt;
    }
}
