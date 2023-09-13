package com.example.hiddenpiece.service.bookmark;

import com.example.hiddenpiece.domain.dto.bookmark.RequestRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.dto.bookmark.ResponseRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.bookmark.RoadmapBookmark;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.bookmark.RoadmapBookmarkRepository;
import com.example.hiddenpiece.domain.repository.roadmap.RoadmapRepository;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoadmapBookmarkService {
    private final RoadmapBookmarkRepository roadmapBookmarkRepository;
    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;

    // 로드맵 북마크 생성
    @Transactional
    public ResponseRoadmapBookmarkDto create(String username, Long roadmapId, RequestRoadmapBookmarkDto dto) {
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));
        // 로드맵 존재 확인
        Roadmap targetRoadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP));

        if (targetRoadmap.getUser().getUsername().equals(username)) {
            throw new CustomException(CustomExceptionCode.CANNOT_BOOKMARK_YOUR_ROADMAP);
        }

        if (roadmapBookmarkRepository.existsByUserAndRoadmap(loginUser, targetRoadmap)) {
            throw new CustomException(CustomExceptionCode.ALREADY_EXIST_ROADMAP_BOOKMARK);
        }

        RoadmapBookmark newRoadmapBookmark = RoadmapBookmark.builder()
                .user(loginUser)
                .roadmap(targetRoadmap)
                .title(dto.getTitle())
                .build();

        roadmapBookmarkRepository.save(newRoadmapBookmark);
        loginUser.addRoadmapBookmarks(newRoadmapBookmark);
        targetRoadmap.addRoadmapBookmarks(newRoadmapBookmark);

        return ResponseRoadmapBookmarkDto.fromEntity(newRoadmapBookmark);
    }

    // 로드맵 북마크 목록 조회
    public Page<ResponseRoadmapBookmarkDto> readAll(String username, Integer page, Integer limit) {
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));

        // 페이징 처리
        Pageable pageable = PageRequest.of(page, limit);
        Page<RoadmapBookmark> roadmapBookmarkPage = roadmapBookmarkRepository.findAllByUser(loginUser, pageable);

        return roadmapBookmarkPage.map(ResponseRoadmapBookmarkDto::fromEntity);
    }

    // 로드맵 북마크 단일 조회
    public ResponseRoadmapBookmarkDto readOne(String username, Long roadmapId) {
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));
        // 로드맵 존재 확인
        Roadmap targetRoadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP));

        RoadmapBookmark targetRoadmapBookmark = roadmapBookmarkRepository.findByUserAndRoadmap(loginUser, targetRoadmap);
        return ResponseRoadmapBookmarkDto.fromEntity(targetRoadmapBookmark);
    }

    // 로드맵 북마크 수정
    @Transactional
    public void update(String username, Long bookmarkId, RequestRoadmapBookmarkDto dto) {
        RoadmapBookmark targetRoadmapBookmark = process(username, bookmarkId);
        // 수정
        targetRoadmapBookmark.update(dto);
        roadmapBookmarkRepository.save(targetRoadmapBookmark);
    }

    // 로드맵 북마크 삭제
    @Transactional
    public void delete(String username, Long bookmarkId) {
        // 삭제
        roadmapBookmarkRepository.delete(process(username, bookmarkId));
    }

    private RoadmapBookmark process(String username, Long bookmarkId) {
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));
        // 삭제할 북마크 존재 확인
        RoadmapBookmark targetRoadmapBookmark = roadmapBookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP_BOOKMARK));
        // 자신의 북마크인지 확인
        if (!targetRoadmapBookmark.getUser().equals(loginUser))
            throw new CustomException(CustomExceptionCode.UNAUTHORIZED_ACCESS);

        return targetRoadmapBookmark;
    }

    public boolean exist(String username, Long roadmapId) {
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));
        // 로드맵 존재 확인
        Roadmap targetRoadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP));

        return roadmapBookmarkRepository.existsByUserAndRoadmap(loginUser, targetRoadmap);
    }
}
