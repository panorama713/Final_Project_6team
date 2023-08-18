package com.example.hiddenpiece.service.roadmap;

import com.example.hiddenpiece.domain.dto.roadmap.RequestRoadmapDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseRoadmapDto;
import com.example.hiddenpiece.domain.entity.roadmap.Roadmap;
import com.example.hiddenpiece.domain.entity.user.User;
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
public class RoadmapService {
    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;

    // create

    // read

    // update
    @Transactional
    public ResponseRoadmapDto update(Long roadmapId, String username, RequestRoadmapDto dto) {
        // 로드맵 유무 확인
        Roadmap targetRoadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP));
        // 로그인 확인
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.INVALID_JWT));
        // 수정자 확인
        if (!targetRoadmap.getUser().equals(loginUser))
            throw new CustomException(CustomExceptionCode.NOT_MATCH_WRITER);
        // 수정
        targetRoadmap.update(dto);
        roadmapRepository.save(targetRoadmap);
        log.info("로드맵 수정 완료");
        return ResponseRoadmapDto.fromEntity(targetRoadmap);
    }

    // delete
}
