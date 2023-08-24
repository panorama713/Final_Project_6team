package com.example.hiddenpiece.service.like;

import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.like.Like;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.community.ArticleRepository;
import com.example.hiddenpiece.domain.repository.like.LikeRepository;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hiddenpiece.exception.CustomExceptionCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Transactional
    public void like(String username, Long articleId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE));

        if (likeRepository.existsByUserAndArticle(user, article)) {
            throw new CustomException(LIKE_FAILED);
        }

        Like like = Like.builder()
                .user(user)
                .article(article).build();

        likeRepository.save(like);

        user.addLikeArticles(like);
        article.addLikeArticles(like);
    }

    @Transactional
    public void unlike(String username, Long articleId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE));

        if (!likeRepository.existsByUserAndArticle(user, article)) {
            throw new CustomException(UNLIKE_FAILED);
        }

        Like like = likeRepository.findByUserAndArticle(user, article)
                .orElseThrow(() -> new CustomException(UNLIKE_FAILED));

        likeRepository.delete(like);
        user.removeLikeArticles(like);
        article.removeLikeArticles(like);
    }

    public int getLikeCount(Article article) {
        return likeRepository.countByArticle(article);
    }
}
