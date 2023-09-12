package com.example.hiddenpiece.controller.roadmap;

import com.example.hiddenpiece.common.ResponseDto;
import com.example.hiddenpiece.common.SystemMessage;
import com.example.hiddenpiece.domain.dto.roadmap.*;
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
public class RoadmapController {
    private final RoadmapService roadmapService;

    // create
    // 로드맵 생성
    @PostMapping
    public ResponseEntity<ResponseRoadmapDto> createRoadmap(
            Authentication authentication,
            @RequestBody RequestRoadmapDto dto
    ) {
        String username = authentication.getName();
        ResponseRoadmapDto responseDto = roadmapService.create(username, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    // read
    @GetMapping
    public ResponseEntity<List<ResponseRoadmapDto>> readAllByUser(
            Authentication authentication,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String type
    ) {
        String username = authentication.getName();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roadmapService.readByUsernameAndYearOrType(username, year, type));
    }

    // update
    // 로드맵 수정
    @PutMapping("/{roadmapId}")
    public ResponseEntity<ResponseRoadmapDto> updateRoadmap(
            Authentication authentication,
            @PathVariable("roadmapId") Long roadmapId,
            @RequestBody RequestRoadmapDto dto
    ) {
        String username = authentication.getName();
        ResponseRoadmapDto responseDto = roadmapService.update(roadmapId, username, dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    // delete
    // 로드맵 삭제
    @DeleteMapping("/{roadmapId}")
    public ResponseEntity<ResponseDto> deleteRoadmap(
            Authentication authentication,
            @PathVariable("roadmapId") Long roadmapId
    ) {
        String username = authentication.getName();
        roadmapService.delete(roadmapId, username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.getInstance(SystemMessage.DELETED_ROADMAP));
    }

    // count
    @GetMapping("/count")
    public ResponseEntity<Integer> countRoadmaps(
            @RequestParam(required = false) String date
    ) {
        if (date != null) {
            return ResponseEntity.ok(roadmapService.countRoadmapsByCreatedAt());
        }

        return ResponseEntity.ok(roadmapService.countRoadmaps());
    }

    @GetMapping("/top5")
    public ResponseEntity<List<ResponseTop5RoadmapDto>> readRoadmapsTop5WithPopularity(
            @RequestParam(required = false) String keyword
    ) {
        if (keyword == null) {
            return ResponseEntity.ok(roadmapService.readTop5RoadmapById());
        }

        if (keyword.equals("popularity")) {
            return ResponseEntity.ok(roadmapService.readTop5RoadmapWithBookmarkCount());
        }

        if (keyword.equals("recommend")) {
            return ResponseEntity.ok(roadmapService.readTop5RoadmapWithRandom());
        }

        return null;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ResponseSearchRoadmapDto>> readAllRoadmapsWithKeyword(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "limit", defaultValue = "7") Integer limit
    ) {
        return ResponseEntity.ok(roadmapService.readAllByContaining(keyword, page, limit));
    }

    // 팔로우 한 유저의 게시글 목록 페이징 조회
    @GetMapping("/following")
    public ResponseEntity<Page<ResponseFollowingRoadmapsDto>> readRoadmapsByFollowings(
            Authentication authentication,
            @RequestParam(name = "num", defaultValue = "0") Integer num,
            @RequestParam(name = "limit", defaultValue = "3") Integer limit
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(roadmapService.readRoadmapsByFollowings(username, num, limit));
    }
}
