package com.example.hiddenpiece.controller.community;
import com.example.hiddenpiece.domain.dto.community.article.*;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.community.Category;
import com.example.hiddenpiece.domain.repository.community.ArticleRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import com.example.hiddenpiece.service.community.ArticleService;
import com.example.hiddenpiece.service.image.ArticleImageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleImageService articleImageService;
    private final ArticleRepository articleRepository;

    // 게시글 작성
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

    // 게시글 리스트 조회
    @GetMapping
    public ResponseEntity<Page<ArticleListResponseDto>> listByCategory(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "category") Category category
    ) {
        return ResponseEntity.ok(articleService.getListByCategory(page, category));
    }

    // 유저가 쓴 게시글 수
    @GetMapping("/countOfArticles")
    public ResponseEntity<Integer> getCountOfArticle (@RequestParam("username") String username) {
        int articleNum = articleService.getCountOfArticles(username);
        return ResponseEntity.ok(articleNum);
    }

    // 유저가 쓴 게시물 목록
    @GetMapping("/userArticles")
    public ResponseEntity<Page<ArticleListResponseDto>> listByUsername(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "username") String username) {
        return ResponseEntity.ok(articleService.getListByUsername(page, username));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ArticleListResponseDto>> searchArticles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam("category") Category category,
            @RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(articleService.searchArticles(page, category, keyword));
    }

    // 통합 검색
    @GetMapping("/total-search")
    public ResponseEntity<Page<ResponseSearchArticleDto>> readAllArticlesWithKeyword(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        return ResponseEntity.ok(articleService.readAllByContaining(keyword, page));
    }

    // 팔로우 한 유저의 게시글 목록 페이징 조회
    @GetMapping("/following")
    public ResponseEntity<Page<ResponseFollowingArticlesDto>> readArticleByFollowings(
            Authentication authentication,
            @RequestParam(name = "num", defaultValue = "0") Integer num,
            @RequestParam(name = "limit", defaultValue = "3") Integer limit
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(articleService.readArticlesByFollowings(username, num, limit));
    }

    // 게시글 단독 조회 (좋아요 개수 포함)
    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponseDto> readArticle(@PathVariable final Long articleId, HttpServletRequest request, HttpServletResponse response) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ARTICLE));

        String sessionKey = "article-" + articleId;

        Cookie[] cookies = request.getCookies();
        boolean alreadyViewed = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (sessionKey.equals(cookie.getName())) {
                    alreadyViewed = true;
                    break;
                }
            }
        }
        if (!alreadyViewed) {
            articleService.increaseViewCount(articleId);
            Cookie viewCookie = new Cookie(sessionKey, "viewed");
            viewCookie.setMaxAge(60 * 60 * 24); // 유효 기간: 1일
            response.addCookie(viewCookie);
        }

        return ResponseEntity.ok(articleService.readArticle(articleId));
    }

    // 게시글 수정
    @PutMapping("/{articleId}")
    public ResponseEntity<Void> updateArticle(
            Authentication authentication,
            @PathVariable final Long articleId,
            @RequestPart final ArticleRequestDto params,
            @RequestPart(required = false) List<MultipartFile> images
    ) throws IOException {
        String username = authentication.getName();
        articleService.updateArticle(username, articleId, params);
        if(images != null && !images.isEmpty()) {
            articleImageService.updateArticleImage(images, username, articleId);
        }
        return ResponseEntity.noContent().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(Authentication authentication, @PathVariable final Long articleId) {
        String username = authentication.getName();
        articleService.deleteArticle(username, articleId);
        return ResponseEntity.noContent().build();
    }
}