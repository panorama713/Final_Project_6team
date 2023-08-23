package com.example.hiddenpiece.controller.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseRoadmapBookmarkDto;
import com.example.hiddenpiece.service.roadmap.RoadmapBookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/roadmaps")
public class RoadmapBookmarkController {
    private final RoadmapBookmarkService roadmapBookmarkService;
///bookmark/{bookmarkId}/roadmaps/{roadmapId}
    // create
    // 로드맵 북마크 생성
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
    // 로드맵 북마크 목록 조회
    @GetMapping("/bookmark")
    public ResponseEntity<Page<ResponseRoadmapBookmarkDto>> readAllRoadmapBookmark(
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ) {
        String username = authentication.getName();
        Page<ResponseRoadmapBookmarkDto> responseDtoPage = roadmapBookmarkService.readAll(username, page, limit);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDtoPage);
    }


    // update
    // 로드맵 북마크 수정(title)
    @PutMapping("/bookmark/{bookmarkId}")
    public ResponseEntity<ResponseRoadmapBookmarkDto> updateRoadmapBookmark(
            Authentication authentication,
            @PathVariable("bookmarkId") Long bookmarkId,
            @RequestBody RequestRoadmapBookmarkDto dto
    ) {
        String username = authentication.getName();
        ResponseRoadmapBookmarkDto responseDto = roadmapBookmarkService.update(username, bookmarkId, dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }


    // delete
    // 로드맵 북마크 삭제
    @DeleteMapping("/bookmark/{bookmarkId}")
    public ResponseEntity<ResponseRoadmapBookmarkDto> deleteRoadmapBookmark(
            Authentication authentication,
            @PathVariable("bookmarkId") Long bookmarkId
    ) {
        String username = authentication.getName();
        ResponseRoadmapBookmarkDto responseDto = roadmapBookmarkService.delete(username, bookmarkId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }
}
