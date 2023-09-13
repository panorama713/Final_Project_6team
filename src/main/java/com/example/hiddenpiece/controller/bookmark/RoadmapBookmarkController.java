package com.example.hiddenpiece.controller.bookmark;

import com.example.hiddenpiece.domain.dto.bookmark.RequestRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.dto.bookmark.ResponseRoadmapBookmarkDto;
import com.example.hiddenpiece.service.bookmark.RoadmapBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "북마크 생성 요청", description = "북마크 생성 기능을 실행합니다.")
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
    @Operation(summary = "특정 유저의 북마크 목록 조회 요청", description = "특정 유저의 북마크 전체 목록을 조회하는 기능을 실행합니다.")
    @GetMapping("/roadmaps")
    public ResponseEntity<Page<ResponseRoadmapBookmarkDto>> readAllRoadmapBookmark(
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(roadmapBookmarkService.readAll(username, page, limit));
    }

    @Operation(summary = "로드맵 북마크 단일 조회 요청", description = "로드맵 북마크를 단일 조회하는 기능을 실행합니다.")
    @GetMapping("/roadmaps/{roadmapId}")
    public ResponseEntity<ResponseRoadmapBookmarkDto> readOneRoadmapBookmark(
            Authentication authentication,
            @PathVariable("roadmapId") Long roadmapId
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(roadmapBookmarkService.readOne(username, roadmapId));
    }

    @Operation(summary = "북마크 내역 확인 요청", description = "북마크한 내역이 있는지 확인하는 기능을 실행합니다.")
    @GetMapping("/roadmaps/{roadmapId}/exist")
    public ResponseEntity<Boolean> existRoadmapBookmark(
            Authentication authentication,
            @PathVariable("roadmapId") Long roadmapId
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(roadmapBookmarkService.exist(username, roadmapId));
    }

    // update
    // 로드맵 북마크 수정(title)
    @Operation(summary = "로드맵 북마크 수정 요청", description = "로드맵 북마크 수정 기능을 실행합니다.")
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
    @Operation(summary = "로드맵 북마크 삭제 요청", description = "로드맵 북마크 삭제 기능을 실행합니다.")
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
