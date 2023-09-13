package com.example.hiddenpiece.controller.roadmap;

import com.example.hiddenpiece.common.ResponseDto;
import com.example.hiddenpiece.common.SystemMessage;
import com.example.hiddenpiece.domain.dto.roadmap.*;
import com.example.hiddenpiece.service.roadmap.RoadmapService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "로드맵 생성 요청", description = "로드맵 생성 기능을 실행합니다.")
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
    @Operation(summary = "나의 모든 로드맵 불러오기 요청", description = "나의 모든 로드맵 조회 기능을 실행합니다.")
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

    // readOne
    // 로드맵 단일 조회
    @Operation(summary = "로드맵 단일 조회 요청", description = "로드맵 단일 조회 기능을 실행합니다.")
    @GetMapping("/{roadmapId}")
    public ResponseEntity<ResponseRoadmapDto> readOneRoadmap(
            Authentication authentication,
            @PathVariable("roadmapId") Long roadmapId
    ) {
        String username = authentication.getName();
        ResponseRoadmapDto responseDto = roadmapService.readOne(username, roadmapId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    // update
    // 로드맵 수정
    @Operation(summary = "로드맵 수정 요청", description = "로드맵 수정 기능을 실행합니다.")
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
    @Operation(summary = "로드맵 삭제 요청", description = "로드맵 삭제 기능을 실행합니다.")
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
    @Operation(summary = "로드맵 전체 개수 조회 요청", description = "로드맵 전체 개수 조회 기능을 실행합니다.")
    @GetMapping("/count")
    public ResponseEntity<Integer> countRoadmaps(
            @RequestParam(required = false) String date
    ) {
        if (date != null) {
            return ResponseEntity.ok((int) roadmapService.countRoadmapsByCreatedAt());
        }

        return ResponseEntity.ok(roadmapService.countRoadmaps());
    }

    @Operation(summary = "인기로드맵 순위 5위까지 조회 요청", description = "인기로드맵 순위 5위까지 조회하여 가져옵니다.")
    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> countRoadmapsByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(roadmapService.countRoadmapsByUserId(userId));
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

    // 통합 검색
    @Operation(summary = "통합 검색 요청(페이징)", description = "통합 검색 기능을 페이징하여 가져옵니다.")
    @GetMapping("/total-search")
    public ResponseEntity<Page<ResponseSearchRoadmapDto>> readAllRoadmapsWithKeyword(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        return ResponseEntity.ok(roadmapService.readAllByContaining(keyword, page));
    }

    // 로드맵 찾기
    @Operation(summary = "로드맵 찾기(검색) 요청", description = "로드맵 찾기(검색) 기능을 페이징하여 가져옵니다.")
    @GetMapping("/search")
    public ResponseEntity<Page<ResponseSearchRoadmapDto>> readAllByTypeOrderBy(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "field", required = false) String field,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        return ResponseEntity.ok(roadmapService.readAllByTypeOrderBy(page, keyword, field, sort));
    }

    // 팔로우 한 유저의 게시글 목록 페이징 조회
    @Operation(summary = "팔로우 한 유저의 게시글 목록 조회 요청(페이징)", description = "팔로우 한 유저의 게시글 목록 조회 기능을 페이징하여 가져옵니다.")
    @GetMapping("/following")
    public ResponseEntity<Page<ResponseFollowingRoadmapsDto>> readRoadmapsByFollowings(
            Authentication authentication,
            @RequestParam(name = "num", defaultValue = "0") Integer num,
            @RequestParam(name = "limit", defaultValue = "3") Integer limit
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(roadmapService.readRoadmapsByFollowings(username, num, limit));
    }

    //유저페이지 내 로드맵 조회
    @GetMapping("/userProfile/{userId}")
    public ResponseEntity<Page<ResponseMyPageRoadmapDto>> readRoadmapsByUserId(@PathVariable("userId") Long userId,
                                                                               @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(roadmapService.readRoadmapsByUserId(userId, page));
    }

    // 마이페이지 내 로드맵 불러오기
    @Operation(summary = "내가 생성한 로드맵 목록 불러오기 요청(페이징)", description = "내가 생성한 로드맵 목록 조회 기능을 페이징하여 가져옵니다.")
    @GetMapping("/my-page")
    public ResponseEntity<Page<ResponseMyPageRoadmapDto>> readMyPageRoadmaps(
            Authentication authentication,
            @RequestParam(name = "num", defaultValue = "0") Integer num,
            @RequestParam(name = "limit", defaultValue = "10") Integer limit
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(roadmapService.readMyPageRoadmaps(username, num, limit));
    }
}
