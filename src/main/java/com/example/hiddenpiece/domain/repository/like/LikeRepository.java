package com.example.hiddenpiece.domain.repository.like;

import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.like.Like;
import com.example.hiddenpiece.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndArticle(User user, Article article);

    Optional<Like> findByUserAndArticle(User user, Article article);

    int countByArticle(Article article);
}
