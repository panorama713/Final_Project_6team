package com.example.hiddenpiece.service.community;
import com.example.hiddenpiece.domain.dto.community.article.ArticleRequestDto;
import com.example.hiddenpiece.domain.dto.community.article.ArticleListResponseDto;
import com.example.hiddenpiece.domain.dto.community.article.ArticleResponseDto;
import com.example.hiddenpiece.domain.dto.community.article.CreateArticleResponseDto;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.community.ArticleRepository;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import com.example.hiddenpiece.service.comment.CommentService;
import com.example.hiddenpiece.service.image.ArticleImageService;
import com.example.hiddenpiece.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final LikeService likeService;
    private final ArticleImageService articleImageService;
    private final CommentService commentService;

    @Transactional
    public CreateArticleResponseDto createArticle(String username, ArticleRequestDto dto) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));

        Article entity = Article.builder()
                .user(loginUser)
                .category(dto.getCategory())
                .title(dto.getTitle())
                .content(dto.getContent())
                .type(dto.getType())
                .build();

        articleRepository.save(entity);
        return CreateArticleResponseDto.fromEntity(entity);
    }

    // 페이징 구현 필요
    // 카테고리별 게시글 목록 조회 구현 필요
    public List<ArticleListResponseDto> readArticles() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id", "createdAt");
        List<Article> list = articleRepository.findAll(sort);
        return list.stream().map(ArticleListResponseDto::new).collect(Collectors.toList());
    }

    // 게시글 단독 조회
    public ArticleResponseDto readArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ARTICLE));

        return ArticleResponseDto.builder()
                .articleId(articleId)
                .username(article.getUser().getUsername())
                .title(article.getTitle())
                .content(article.getContent())
                .type(article.getType())
                .createdAt(article.getCreatedAt())
                .createdAt(article.getLastModifiedAt())
                .viewCount(article.getViewCount())
                .likeCount(likeService.getLikeCount(article))
                .images(articleImageService.readAllArticleImages(articleId))
                .comments(commentService.readAllCommentsForArticle(articleId))
                .build();
    }

    @Transactional
    public void updateArticle(String username, final Long articleId, final ArticleRequestDto dto) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));

        Article target = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ARTICLE));

        if (!target.getUser().equals(loginUser)) {
            throw new CustomException(CustomExceptionCode.NOT_MATCH_WRITER);
        }

        target.modify(dto.getTitle(), dto.getContent(), dto.getType());
    }

    @Transactional
    public void deleteArticle(String username, final Long articleId) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));

        Article target = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ARTICLE));

        if (!target.getUser().equals(loginUser)) {
            throw new CustomException(CustomExceptionCode.NOT_MATCH_WRITER);
        }

        articleImageService.deleteArticleImage(username, articleId);
        articleRepository.deleteById(articleId);
    }

}
