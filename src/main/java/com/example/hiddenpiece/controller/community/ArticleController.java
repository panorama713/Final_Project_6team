package com.example.hiddenpiece.controller.community;
import com.example.hiddenpiece.domain.dto.community.ArticleRequestDto;
import com.example.hiddenpiece.domain.dto.community.ArticleResponseDto;
import com.example.hiddenpiece.service.community.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("")
    public Long createArticle(Authentication authentication, @RequestBody final ArticleRequestDto params) {
        String username = authentication.getName();
        return articleService.createArticle(username, params);
    }

    @GetMapping("")
    public List<ArticleResponseDto> findAll() {
        return articleService.findAll();
    }

    @PutMapping("/{id}")
    public Long updateArticle(Authentication authentication, @PathVariable final Long id, @RequestBody final ArticleRequestDto params) {
        String username = authentication.getName();
        return articleService.updateArticle(username, id, params);
    }

    @DeleteMapping("/{id}")
    public Long deleteArticle(Authentication authentication, @PathVariable final Long id) {
        String username = authentication.getName();
        return articleService.deleteArticle(username, id);
    }
}
