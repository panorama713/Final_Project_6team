package com.example.hiddenpiece.domain.repository.comment;

import com.example.hiddenpiece.domain.entity.comment.Comment;
import com.example.hiddenpiece.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Long articleId);
//    Page<Comment> findCommentsByUser(User user, Pageable pageable);

}
