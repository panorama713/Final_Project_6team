package com.example.hiddenpiece.controller.community;
import com.example.hiddenpiece.domain.dto.community.ArticleRequestDto;
import com.example.hiddenpiece.domain.dto.community.ArticleResponseDto;
import com.example.hiddenpiece.domain.dto.roadmap.ResponseRoadmapDto;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import com.example.hiddenpiece.service.community.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/article")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/write")
    public Long createArticle(Authentication authentication, @RequestBody final ArticleRequestDto params) {
        String username = authentication.getName();
        return articleService.createArticle(username, params);
    }

    @GetMapping("/articles")
    public List<ArticleResponseDto> findAll() {
        return articleService.findAll();
    }

    @PatchMapping("/articles/{id}")
    public Long updateArticle(Authentication authentication, @PathVariable final Long id, @RequestBody final ArticleRequestDto params) {
        String username = authentication.getName();
        return articleService.updateArticle(username, id, params);
    }

    @DeleteMapping("/articles/delete/{id}")
    public Long deleteArticle(Authentication authentication, @PathVariable final Long id) {
        String username = authentication.getName();
        return articleService.deleteArticle(username, id);
    }
}
