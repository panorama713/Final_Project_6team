package com.example.hiddenpiece.domain.dto.community.comment;

import com.example.hiddenpiece.domain.entity.comment.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

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

    @JsonInclude(JsonInclude.Include.NON_NULL) // 대댓글의 대댓글 목록을 JSON 응답에서 제외
    private List<CommentResponseDto> replies;

    public void setReplies(List<CommentResponseDto> replies) {
        this.replies = replies;
    }
}
