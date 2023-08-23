package com.example.hiddenpiece.service.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseRoadmapBookmarkDto;
import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.roadmap.RoadmapBookmark;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.roadmap.RoadmapBookmarkRepository;
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

    // 로드맵 북마크 생성/취소
    @Transactional
    public ResponseRoadmapBookmarkDto create(String username, Long roadmapId, RequestRoadmapBookmarkDto dto) {
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));
        // 로드맵 존재 확인
        Roadmap targetRoadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP));

        // 해당 로드맵이 북마크가 되어 있다면 취소
        if (roadmapBookmarkRepository.existsByUserAndRoadmap(loginUser, targetRoadmap)) {
            RoadmapBookmark bookmark = roadmapBookmarkRepository.findByUserAndRoadmap(loginUser, targetRoadmap);
            roadmapBookmarkRepository.delete(bookmark);
            return ResponseRoadmapBookmarkDto.fromEntityDelete(bookmark);
        } else {
            RoadmapBookmark newRoadmapBookmark = RoadmapBookmark.builder()
                    .title(dto.getTitle().isBlank() ? targetRoadmap.getTitle() : dto.getTitle())
                    .user(loginUser)
                    .roadmap(targetRoadmap)
                    .build();
            roadmapBookmarkRepository.save(newRoadmapBookmark);

            return ResponseRoadmapBookmarkDto.fromEntity(newRoadmapBookmark);
        }
    }

    // 로드맵 북마크 목록 조회
    public Page<ResponseRoadmapBookmarkDto> readAll(String username, Integer page, Integer limit) {
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));

        // 페이징 처리
        Pageable pageable = PageRequest.of(page, limit);
        Page<RoadmapBookmark> roadmapBookmarkPage = roadmapBookmarkRepository.findAllByUser(loginUser, pageable);
        Page<ResponseRoadmapBookmarkDto> roadmapBookmarkDtoPage = roadmapBookmarkPage.map(ResponseRoadmapBookmarkDto::fromEntity);

        return roadmapBookmarkDtoPage;
    }
}
