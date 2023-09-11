package com.example.hiddenpiece.domain.dto.user;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseFollowerDto {
    private Long toUserId;
    private String username;
    private LocalDateTime createdAt;

    @Builder
    public ResponseFollowerDto(Long toUserId, String username, LocalDateTime createdAt) {
        this.toUserId = toUserId;
        this.username = username;
        this.createdAt = createdAt;
    }
}
