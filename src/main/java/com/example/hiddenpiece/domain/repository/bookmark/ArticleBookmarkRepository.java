package com.example.hiddenpiece.domain.repository.bookmark;

import com.example.hiddenpiece.domain.entity.bookmark.ArticleBookmark;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleBookmarkRepository extends JpaRepository<ArticleBookmark, Long> {
    Page<ArticleBookmark> findAllByUser(User user, Pageable pageable);

    boolean existsByUserAndArticle(User user, Article article);
}
