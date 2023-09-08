package com.example.hiddenpiece.service.community;
import com.example.hiddenpiece.domain.dto.community.article.ArticleRequestDto;
import com.example.hiddenpiece.domain.dto.community.article.ArticleListResponseDto;
import com.example.hiddenpiece.domain.dto.community.article.ArticleResponseDto;
import com.example.hiddenpiece.domain.dto.community.article.CreateArticleResponseDto;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.community.Category;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.community.ArticleRepository;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import com.example.hiddenpiece.service.comment.CommentService;
import com.example.hiddenpiece.service.image.ArticleImageService;
import com.example.hiddenpiece.service.like.LikeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    private final EntityManager entityManager;

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

    // 모든 게시글 목록 조회
    public Page<ArticleListResponseDto> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return articleRepository.findAll(pageable).map(ArticleListResponseDto::new);
    }


    // 카테고리 게시글 목록 조회
    public Page<ArticleListResponseDto> getListByCategory(int page, Category category) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());

        String jpql = "SELECT a, (SELECT COUNT(c) FROM Comment c WHERE c.article = a AND c.parentComment IS NULL) " +
                "FROM Article a " +
                "WHERE a.category = :category " +
                "ORDER BY a.createdAt DESC";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("category", category);

        List<ArticleListResponseDto> result = getArticlesWithCommentCount(jpql, parameters);

        return new PageImpl<>(result, pageable, result.size());
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
                .category(article.getCategory())
                .createdAt(article.getCreatedAt())
                .lastModifiedAt(article.getLastModifiedAt())
                .viewCount(article.getViewCount())
                .likeCount(likeService.getLikeCount(article))
                .images(articleImageService.readAllArticleImages(articleId))
                .comments(commentService.readAllCommentsForArticle(articleId))
                .build();
    }

    public List<ArticleListResponseDto> searchArticles(String keyword, Category category) {

        String jpql = "SELECT a, (SELECT COUNT(c) FROM Comment c WHERE c.article = a AND c.parentComment IS NULL) " +
                "FROM Article a " +
                "WHERE (a.title LIKE :keyword OR a.content LIKE :keyword) AND a.category = :category " +
                "ORDER BY a.createdAt DESC";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "%" + keyword + "%");
        parameters.put("category", category);

        return getArticlesWithCommentCount(jpql, parameters);
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

        target.modify(dto.getTitle(), dto.getContent(), dto.getType(), dto.getCategory());
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

    @Transactional
    public void increaseViewCount(Long articleId) {
        Query query = entityManager.createQuery("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.id = :articleId");
        query.setParameter("articleId", articleId);
        query.executeUpdate();
    }

    /**
     * 게시글과 해당하는 댓글 수를 함께 조회하는 메서드
     * @param jpql           실행할 JPQL 쿼리 문자열
     * @param parameters     JPQL 쿼리의 파라미터와 해당하는 값을 매핑한 Map
     * @return               게시글 정보와 해당하는 댓글 수를 포함한 DTO 리스트
     */
    private List<ArticleListResponseDto> getArticlesWithCommentCount(String jpql, Map<String, Object> parameters) {
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<ArticleListResponseDto> result = query.getResultList().stream()
                .map(row -> {
                    Article article = (Article) row[0];
                    int commentCount = ((Number) row[1]).intValue();
                    return new ArticleListResponseDto(article, commentCount);
                }).collect(Collectors.toList());

        return result;
    }
}
