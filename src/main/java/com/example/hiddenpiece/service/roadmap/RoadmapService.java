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
        // 로드맵 존재 확인
        Roadmap targetRoadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ROADMAP));
        // TODO: 로그인 확인
//        User loginUser = userRepository.findByUsername(username)
//                .orElseThrow(() -> new CustomException(CustomExceptionCode.INVALID_JWT));
        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));
        // 작성자 확인
        if (!targetRoadmap.getUser().equals(targetUser))
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
        // TODO: 로그인 확인
//        User loginUser = userRepository.findByUsername(username)
//                .orElseThrow(() -> new CustomException(CustomExceptionCode.INVALID_JWT));
        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));
        // 작성자 확인
        if (!targetRoadmap.getUser().equals(targetUser))
            throw new CustomException(CustomExceptionCode.NOT_MATCH_WRITER);
        // 삭제
        roadmapRepository.delete(targetRoadmap);
        log.info("로드맵 삭제 완료");
        return ResponseRoadmapDto.fromEntity(targetRoadmap);
    }
}
