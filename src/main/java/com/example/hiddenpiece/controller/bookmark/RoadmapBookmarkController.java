package com.example.hiddenpiece.controller.bookmark;

import com.example.hiddenpiece.domain.dto.bookmark.RequestRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.dto.bookmark.ResponseRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseRoadmapDto;
import com.example.hiddenpiece.service.bookmark.RoadmapBookmarkService;
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
@RequestMapping("/api/v1/bookmarks")
public class RoadmapBookmarkController {
    private final RoadmapBookmarkService roadmapBookmarkService;

    // create
    // 로드맵 북마크 생성
    @PostMapping("/roadmaps/{roadmapId}")
    public ResponseEntity<ResponseRoadmapBookmarkDto> createRoadmapBookmark(
            Authentication authentication,
            @PathVariable("roadmapId") Long roadmapId,
            @RequestBody RequestRoadmapBookmarkDto dto
    ) {
        String username = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(roadmapBookmarkService.create(username, roadmapId, dto));
    }

    // readAll
    // 로드맵 북마크 목록 조회
    @GetMapping("/roadmaps")
    public ResponseEntity<Page<ResponseRoadmapBookmarkDto>> readAllRoadmapBookmark(
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(roadmapBookmarkService.readAll(username, page, limit));
    }

    // update
    // 로드맵 북마크 수정(title)
    @PutMapping("/{bookmarkId}/roadmaps")
    public ResponseEntity<Void> updateRoadmapBookmark(
            Authentication authentication,
            @PathVariable("bookmarkId") Long bookmarkId,
            @RequestBody RequestRoadmapBookmarkDto dto
    ) {
        String username = authentication.getName();
        roadmapBookmarkService.update(username, bookmarkId, dto);

        return ResponseEntity.noContent().build();
    }

    // delete
    // 로드맵 북마크 삭제
    @DeleteMapping("/{bookmarkId}/roadmaps")
    public ResponseEntity<Void> deleteRoadmapBookmark(
            Authentication authentication,
            @PathVariable("bookmarkId") Long bookmarkId
    ) {
        String username = authentication.getName();
        roadmapBookmarkService.delete(username, bookmarkId);
        return ResponseEntity.noContent().build();
    }
}
