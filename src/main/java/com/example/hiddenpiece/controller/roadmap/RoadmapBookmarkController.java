package com.example.hiddenpiece.controller.roadmap;

import com.example.hiddenpiece.common.ResponseDto;
import com.example.hiddenpiece.common.SystemMessage;
import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseRoadmapDto;
import com.example.hiddenpiece.service.roadmap.RoadmapBookmarkService;
import com.example.hiddenpiece.service.roadmap.RoadmapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/roadmaps")
public class RoadmapBookmarkController {
    private final RoadmapBookmarkService roadmapBookmarkService;

    // create
    // 로드맵 생성
    @PostMapping("/{roadmapId}/bookmark")
    public ResponseEntity<ResponseRoadmapBookmarkDto> createRoadmapBookmark(
            Authentication authentication,
            @PathVariable("roadmapId") Long roadmapId,
            @RequestBody RequestRoadmapBookmarkDto dto
    ) {
        String username = authentication.getName();
        ResponseRoadmapBookmarkDto responseDto = roadmapBookmarkService.create(username, roadmapId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    // readAll
    @GetMapping("/bookmark")
    public ResponseEntity<Page<ResponseRoadmapBookmarkDto>> readAllRoadmapBookmark(
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0")Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ) {
        String username = authentication.getName();
        Page<ResponseRoadmapBookmarkDto> responseDtoPage = roadmapBookmarkService.readAll(username, page, limit);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDtoPage);
    }


    // update


    // delete

}
