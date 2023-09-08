package com.example.hiddenpiece.domain.repository.community;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.community.Category;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 전체 검색
    List<Article> findByTitleContainingOrContentContaining(String keyword1, String keyword2);

    // 카테고리 별 검색
    List<Article> findByCategoryAndTitleContainingOrCategoryAndContentContaining(Category category1, String keyword1, Category category2, String keyword2);
    Page<Article> findAll(Pageable pageable);
    Page<Article> findByCategory(Category category, Pageable pageable);

}
