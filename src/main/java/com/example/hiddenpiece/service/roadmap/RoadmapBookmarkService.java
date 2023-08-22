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

    @Transactional
    public ResponseRoadmapBookmarkDto create(String username, Long roadmapId, RequestRoadmapBookmarkDto dto) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.INVALID_JWT));
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
}
