package com.example.hiddenpiece.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRoadmapElementDto {
    @NotBlank(message = "제목을 입력해 주세요")
    private String title;
    @NotBlank(message = "내용을 입력해 주세요")
    private String content;
    @NotNull(message = "시작일을 선택해 주세요")
    private LocalDateTime startDate;
    @NotNull(message = "종료일을 선택해 주세요")
    private LocalDateTime endDate;
}
