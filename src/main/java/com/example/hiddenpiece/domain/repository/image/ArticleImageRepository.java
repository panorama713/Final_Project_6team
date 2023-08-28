package com.example.hiddenpiece.domain.repository.image;

import com.example.hiddenpiece.domain.entity.image.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
    List<ArticleImage> findAllByArticleId(Long articleId);
}
