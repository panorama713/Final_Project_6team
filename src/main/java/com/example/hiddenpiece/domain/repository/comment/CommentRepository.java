package com.example.hiddenpiece.domain.repository.comment;

import com.example.hiddenpiece.domain.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Long articleId);

}
