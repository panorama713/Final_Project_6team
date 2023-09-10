package com.example.hiddenpiece.service.bookmark;

import com.example.hiddenpiece.domain.dto.bookmark.RequestArticleBookmarkDto;
import com.example.hiddenpiece.domain.dto.bookmark.ResponseArticleBookmarkDto;
import com.example.hiddenpiece.domain.entity.bookmark.ArticleBookmark;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.bookmark.ArticleBookmarkRepository;
import com.example.hiddenpiece.domain.repository.community.ArticleRepository;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleBookmarkService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleBookmarkRepository articleBookmarkRepository;

    // 게시글 북마크 생성
    @Transactional
    public ResponseArticleBookmarkDto createArticleBookmark(String username, Long articleId, RequestArticleBookmarkDto dto) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));

        Article targetArticle = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ARTICLE));

        if (articleBookmarkRepository.existsByUserAndArticle(loginUser, targetArticle)) {
            throw new CustomException(CustomExceptionCode.ALREADY_EXIST_ARTICLE_BOOKMARK);
        }

        if (loginUser == targetArticle.getUser()) {
            throw new CustomException(CustomExceptionCode.CANNOT_BOOKMARK_YOUR_ARTICLE);
        }

        ArticleBookmark articleBookmark = ArticleBookmark.builder()
                .user(loginUser)
                .article(targetArticle)
                .title(dto.getTitle())
                .build();

        articleBookmarkRepository.save(articleBookmark);
        loginUser.addArticleBookmarks(articleBookmark);
        targetArticle.addArticleBookmarks(articleBookmark);

        return ResponseArticleBookmarkDto.fromEntity(articleBookmark);
    }

    // 북마크한 게시글 목록 조회
    public Page<ResponseArticleBookmarkDto> readAllArticleByBookmark(String username, Integer page, Integer limit) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));

        Pageable pageable = PageRequest.of(page, limit);
        Page<ArticleBookmark> articleBookmarkPage = articleBookmarkRepository.findAllByUser(loginUser, pageable);

        return articleBookmarkPage.map(ResponseArticleBookmarkDto::fromEntity);
    }

    // 게시글 북마크 수정
    @Transactional
    public void updateArticleBookmark(String username, Long bookmarkId, RequestArticleBookmarkDto dto) {
        ArticleBookmark articleBookmark = process(username, bookmarkId);
        articleBookmark.updateTitle(dto.getTitle());
        articleBookmarkRepository.save(articleBookmark);
    }

    // 게시글 북마크 삭제
    @Transactional
    public void deleteArticleBookmark(String username, Long articleId) {
        articleBookmarkRepository.delete(process(username, articleId));
    }

    private ArticleBookmark process(String username, Long articleId) {
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));

        ArticleBookmark articleBookmark = articleBookmarkRepository.findByArticleId(articleId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ARTICLE_BOOKMARK));

        if (!articleBookmark.getUser().equals(loginUser)) {
            throw new CustomException(CustomExceptionCode.NOT_MATCH_WRITER);
        }

        return articleBookmark;
    }

}
