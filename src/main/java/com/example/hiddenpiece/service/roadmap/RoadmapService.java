package com.example.hiddenpiece.service.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.*;
import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.bookmark.RoadmapBookmarkRepository;
import com.example.hiddenpiece.domain.repository.follow.FollowRepository;
import com.example.hiddenpiece.domain.repository.roadmap.RoadmapRepository;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.hiddenpiece.exception.CustomExceptionCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoadmapService {
    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;
    private final RoadmapBookmarkRepository roadmapBookmarkRepository;
    private final FollowRepository followRepository;

    // create
    @Transactional
    public ResponseRoadmapDto create(String username, RequestRoadmapDto dto) {
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.INVALID_JWT));

        // 생성
        Roadmap newRoadmap = Roadmap.builder()
                .user(loginUser)
                .type(dto.getType())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
        roadmapRepository.save(newRoadmap);
        return ResponseRoadmapDto.fromEntity(newRoadmap);
    }

    // read
    public List<ResponseRoadmapDto> readByUsernameAndYearOrType(String username, Integer year, String type) {
        // 로그인 확인
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(INVALID_JWT));
        log.info("유저 확인 완료");

        // 키워드에 맞는 로드맵 찾기
        // 키워드에 맞는 targetDate 생성

        List<Roadmap> roadmaps;
        // year 가 null 이 아닌 경우
        if (year != null) {
            LocalDateTime targetDate = LocalDateTime.of(year, 1, 1, 0, 0, 0);
            LocalDateTime targetYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);
            roadmaps = roadmapRepository.findRoadmapByUserAndCreatedAtAndType(user, targetDate, targetYear, type);
        } else {
            // null 인 경우
            roadmaps = roadmapRepository.findRoadmapByUserAndCreatedAtAndType(user, null, null, type);
        }

        // 해당 조건의 로드맵이 없을 경우
        if (roadmaps.isEmpty()) throw new CustomException(NOT_FOUND_CONDITION_ROADMAP);

        // dto 변환
        List<ResponseRoadmapDto> roadmapDtos = new ArrayList<>();
        for (Roadmap roadmap : roadmaps) {
            roadmapDtos.add(ResponseRoadmapDto.fromEntity(roadmap));
        }

        log.info("나의 로드맵 찾기 완료");
        return roadmapDtos;
    }

    // readOne
    // 로드맵 단일 조회
    public ResponseRoadmapDto readOne(String username, Long roadmapId) {
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.INVALID_JWT));
        // 로드맵 화인
        Roadmap targetRoadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ROADMAP));
        User writer = userRepository.findByUsername(targetRoadmap.getUser().getUsername())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        boolean isFollow = followRepository.existsByToUserAndFromUser(writer, loginUser);

        return ResponseRoadmapDto.fromEntity(targetRoadmap, isFollow);
    }

    // update
    @Transactional
    public ResponseRoadmapDto update(Long roadmapId, String username, RequestRoadmapDto dto) {
        // 로드맵 존재 확인
        Roadmap targetRoadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP));
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.INVALID_JWT));
        // 작성자 확인
        if (!targetRoadmap.getUser().equals(loginUser))
            throw new CustomException(CustomExceptionCode.NOT_MATCH_WRITER);
        // 수정
        targetRoadmap.update(dto);
        roadmapRepository.save(targetRoadmap);
        log.info("로드맵 수정 완료");
        return ResponseRoadmapDto.fromEntity(targetRoadmap);
    }

    // delete
    @Transactional
    public ResponseRoadmapDto delete(Long roadmapId, String username) {
        // 로드맵 존재 확인
        Roadmap targetRoadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP));
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.INVALID_JWT));
        // 작성자 확인
        if (!targetRoadmap.getUser().equals(loginUser))
            throw new CustomException(CustomExceptionCode.NOT_MATCH_WRITER);
        // 삭제
        roadmapRepository.delete(targetRoadmap);
        log.info("로드맵 삭제 완료");
        return ResponseRoadmapDto.fromEntity(targetRoadmap);
    }

    // 로드맵 전체 개수 카운트
    public Integer countRoadmaps() {
        return (int) roadmapRepository.count();
    }

    // 오늘 날짜 기준 생성된 로드맵 개수 카운트
    public long countRoadmapsByCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        return roadmapRepository.countTodayRoadmaps(startOfDay, endOfDay);
    }

    // 유저 로드맵 개수 카운트
    public int countRoadmapsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        return roadmapRepository.countByUser(user);
    }

    // 추천 로드맵 조회
    // TODO 우선은 랜덤 -> 기준을 유저가 설정한 타입으로 하기로 했는데 회원가입 시 추가하는 부분이 없어서 추후 수정해야함
    public List<ResponseTop5RoadmapDto> readTop5RoadmapWithRandom() {
        return roadmapRepository.findTop5ByRoadmapsWithRandom();
    }

    // 인기 로드맵 조회 (북마크 저장 기준)
    public List<ResponseTop5RoadmapDto> readTop5RoadmapWithBookmarkCount() {
        return roadmapBookmarkRepository.findTop5ByRoadmapsWithBookmarkCount();
    }

    // 신규 로드맵 조회
    public List<ResponseTop5RoadmapDto> readTop5RoadmapById() {
        return roadmapRepository.findTop5ByRoadmapsWithId();
    }

    // 통합 검색 로드맵 조회
    public Page<ResponseSearchRoadmapDto> readAllByContaining(String keyword, Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        return roadmapRepository.findByContaining(keyword, pageable);
    }

    // 로드맵 찾기 페이지 조회
    public Page<ResponseSearchRoadmapDto> readAllByTypeOrderBy(Integer page, String keyword, String field, String sort) {
        Sort pageableSort = Sort.by("createdAt");

        pageableSort = "newest".equals(sort) ? pageableSort.descending() : pageableSort.ascending();

        Pageable pageable = PageRequest.of(page, 9, pageableSort);
        return roadmapRepository.findRoadmapByTypeOrderBySort(keyword, field, pageable);
    }

    // 팔로우 한 유저의 로드맵 조회
    public Page<ResponseFollowingRoadmapsDto> readRoadmapsByFollowings(String username, Integer num, Integer limit) {
        User currentUser = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        Pageable pageable = PageRequest.of(num, limit);

        Page<Roadmap> roadmapPage = followRepository.findRoadmapsByFromUserFollowing(currentUser, pageable);
        return roadmapPage.map(ResponseFollowingRoadmapsDto::fromEntity);
    }

    // 유저 프로필 내 로드맵 조회
    public Page<ResponseMyPageRoadmapDto> readRoadmapsByUserId(Long userId, int page) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        return roadmapRepository.findRoadmapByUser(user, pageable);
    }

    // 마이페이지 내 로드맵 조회
    public Page<ResponseMyPageRoadmapDto> readMyPageRoadmaps(String username, Integer num, Integer limit) {
        User targetUser = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        Pageable pageable = PageRequest.of(num, limit);

        return roadmapRepository.findRoadmapByUser(targetUser, pageable);
    }
}
