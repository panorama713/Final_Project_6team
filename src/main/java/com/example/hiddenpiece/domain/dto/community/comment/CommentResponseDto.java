package com.example.hiddenpiece.domain.dto.community.comment;

import com.example.hiddenpiece.domain.entity.comment.Comment;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String username;
    private String content;

    public static CommentResponseDto fromEntity(Comment entity) {
        return CommentResponseDto.builder()
                .id(entity.getId())
                .username(entity.getUser().getUsername())
                .content(entity.getContent())
                .build();
    }
}
