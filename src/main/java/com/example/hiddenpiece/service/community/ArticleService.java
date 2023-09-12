package com.example.hiddenpiece.service.community;
import com.example.hiddenpiece.domain.dto.community.article.*;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.community.Category;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.bookmark.ArticleBookmarkRepository;
import com.example.hiddenpiece.domain.repository.community.ArticleRepository;
import com.example.hiddenpiece.domain.repository.follow.FollowRepository;
import com.example.hiddenpiece.domain.repository.like.LikeRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hiddenpiece.exception.CustomExceptionCode.NOT_FOUND_USER;

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
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;
    private final ArticleBookmarkRepository articleBookmarkRepository;

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

    // 카테고리별 게시글 목록 조회
    public Page<ArticleListResponseDto> getListByCategory(int page, Category category) {
        String jpql = "SELECT a, (SELECT COUNT(c) FROM Comment c WHERE c.article = a AND c.parentComment IS NULL) " +
                "FROM Article a " +
                "WHERE a.category = :category " +
                "ORDER BY a.createdAt DESC";
        String countJpql = "SELECT COUNT(a) FROM Article a WHERE a.category = :category";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("category", category);

        return getArticlesAndPage(jpql, countJpql, parameters, page);
    }

    public Page<ArticleListResponseDto> getListByUsername(int page, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        String jpql = "SELECT a, (SELECT COUNT(c) FROM Comment c WHERE c.article = a AND c.parentComment IS NULL) " +
                "FROM Article a " +
                "WHERE a.user = :user " +
                "ORDER BY a.createdAt DESC";

        String countJpql = "SELECT COUNT(a) FROM Article a WHERE a.user = :user";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user", user);
        return getArticlesAndPage(jpql, countJpql, parameters, page);
    }

    // 유저가 쓴 게시글 개수
    public int getCountOfArticles(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        return articleRepository.countByUser(user);

    }

    // 게시글 단독 조회
    public ArticleResponseDto readArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ARTICLE));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isCurrentWriter = article.getUser().getUsername().equals(currentUsername);

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        User Writer = userRepository.findByUsername(article.getUser().getUsername())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        boolean isLike = likeRepository.existsByUserAndArticle(currentUser, article);
        boolean isFollow = followRepository.existsByToUserAndFromUser(Writer, currentUser);
        boolean isBookmark = articleBookmarkRepository.existsByUserAndArticle(currentUser, article);

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
                .isWriter(isCurrentWriter)
                .isLike(isLike)
                .isFollow(isFollow)
                .isBookmark(isBookmark)
                .build();
    }

    // 카테고리별 키워드 검색 이후 게시글 목록 조회
    public Page<ArticleListResponseDto> searchArticles(int page, Category category, String keyword) {
        String jpql = "SELECT a, (SELECT COUNT(c) FROM Comment c WHERE c.article = a AND c.parentComment IS NULL) " +
                "FROM Article a " +
                "WHERE (a.title LIKE :keyword OR a.content LIKE :keyword) AND a.category = :category " +
                "ORDER BY a.createdAt DESC";
        String countJpql = "SELECT COUNT(a) FROM Article a WHERE (a.title LIKE :keyword OR a.content LIKE :keyword) AND a.category = :category";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("keyword", "%" + keyword + "%");
        parameters.put("category", category);

        return getArticlesAndPage(jpql, countJpql, parameters, page);
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

    // 게시글과 해당 게시글의 댓글 수를 함께 조회
    private List<ArticleListResponseDto> getArticlesWithCommentCount(String jpql, Map<String, Object> parameters, Pageable pageable) {
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ArticleListResponseDto> result = query.getResultList().stream()
                .map(row -> {
                    Article article = (Article) row[0];
                    int commentCount = ((Number) row[1]).intValue();
                    return new ArticleListResponseDto(article, commentCount);
                }).collect(Collectors.toList());

        return result;
    }

    // 게시글 목록과 페이지 정보를 반환
    private Page<ArticleListResponseDto> getArticlesAndPage(String jpql, String countJpql, Map<String, Object> parameters, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        List<ArticleListResponseDto> result = getArticlesWithCommentCount(jpql, parameters, pageable);
        long totalCount = getCount(countJpql, parameters);

        return new PageImpl<>(result, pageable, totalCount);
    }

    // JPQL 쿼리를 통해 항목 수를 반환
    private long getCount(String jpql, Map<String, Object> parameters) {
        TypedQuery<Long> countQuery = entityManager.createQuery(jpql, Long.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        return countQuery.getSingleResult();
    }

    public Page<ResponseFollowingArticlesDto> readArticlesByFollowings(String username, Integer num, Integer limit) {
        User currentUser = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        Pageable pageable = PageRequest.of(num, limit);

        Page<Article> articlePage = followRepository.findArticlesByFromUserFollowing(currentUser, pageable);
        return articlePage.map(ResponseFollowingArticlesDto::fromEntity);
    }
}
