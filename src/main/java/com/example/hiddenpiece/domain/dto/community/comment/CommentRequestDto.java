package com.example.hiddenpiece.domain.dto.community.comment;

import com.example.hiddenpiece.domain.entity.comment.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentRequestDto {
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    public static CommentRequestDto fromEntity(Comment entity) {
        return CommentRequestDto.builder()
                .content(entity.getContent())
                .build();
    }
}
