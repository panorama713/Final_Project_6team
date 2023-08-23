package com.example.hiddenpiece.domain.repository.community;
import com.example.hiddenpiece.domain.entity.community.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
