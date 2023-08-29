package com.example.hiddenpiece.controller.community;

import com.example.hiddenpiece.domain.dto.community.article.ArticleListResponseDto;
import com.example.hiddenpiece.domain.dto.community.article.ArticleRequestDto;
import com.example.hiddenpiece.domain.dto.community.article.ArticleResponseDto;
import com.example.hiddenpiece.domain.dto.community.article.CreateArticleResponseDto;
import com.example.hiddenpiece.service.community.ArticleService;
import com.example.hiddenpiece.service.image.ArticleImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleImageService articleImageService;

    @PostMapping
    public ResponseEntity<CreateArticleResponseDto> createArticle(
            Authentication authentication,
            @RequestPart ArticleRequestDto params,
            @RequestPart(required = false) List<MultipartFile> images
    ) throws IOException {
        String username = authentication.getName();
        CreateArticleResponseDto responseDto = articleService.createArticle(username, params);
        if (images != null && !images.isEmpty()) {
            articleImageService.createArticleImage(images, username, responseDto.getId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        }

    @GetMapping("")
    public String createArticles() {
        return "article-write";
    }

    @GetMapping("/list")
    public String readAllArticles(Model model) {
        model.addAttribute("articles", articleService.readArticles());
        return "article-list";
    }

    @GetMapping("/{articleId}")
    public String readArticle(Model model, @PathVariable final Long articleId) {
        model.addAttribute("article",articleService.readArticle(articleId));
        return "article-detail";
    }


    @GetMapping("/get/{articleId}")
    public String updateArticle(Model model, @PathVariable final Long articleId) {
        model.addAttribute("article",articleService.readArticle(articleId));
        return "article-update";
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<Object> updateArticle(
            Authentication authentication,
            @PathVariable final Long articleId,
            @RequestPart final ArticleRequestDto params,
            @RequestPart(required = false) List<MultipartFile> images
    ) throws IOException {
        String username = authentication.getName();
        articleService.updateArticle(username, articleId, params);
        if (images != null && !images.isEmpty()) {
            articleImageService.updateArticleImage(images, username, articleId);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(Authentication authentication, @PathVariable final Long articleId) {
        String username = authentication.getName();
        articleService.deleteArticle(username, articleId);
        return "redirect:/api/v1/articles/list";
    }

    @PutMapping("/{articleId}/images")
    public ResponseEntity<Void> updateSpecificImage(
            @RequestPart List<Long> imageIds,
            @RequestPart List<MultipartFile> images,
            @PathVariable final Long articleId,
            Authentication authentication
    ) throws IOException {
        String username = authentication.getName();
        articleImageService.updateSpecificImage(imageIds, images, username, articleId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{articleId}/images")
    public ResponseEntity<Void> deleteSpecificImages(
            @RequestPart List<Long> imageIds,
            @PathVariable final Long articleId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        articleImageService.deleteSpecificImage(imageIds, username, articleId);
        return ResponseEntity.noContent().build();
    }
}
