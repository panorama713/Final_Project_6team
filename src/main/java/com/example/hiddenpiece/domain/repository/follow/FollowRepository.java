package com.example.hiddenpiece.domain.repository.follow;

import com.example.hiddenpiece.domain.dto.user.ResponseFollowerDto;
import com.example.hiddenpiece.domain.dto.user.ResponseFollowingDto;
import com.example.hiddenpiece.domain.entity.community.Article;
import com.example.hiddenpiece.domain.entity.follow.Follow;
import com.example.hiddenpiece.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByToUserAndFromUser(User toUser, User fromUser);

    boolean existsByToUserAndFromUser(User toUser, User fromUser);

    int countByFromUser(User fromUser);
    int countByToUser(User toUser);

    @Query("SELECT new com.example.hiddenpiece.domain.dto.user.ResponseFollowingDto(f.toUser.id, f.toUser.username, f.createdAt) " +
            "FROM Follow f " +
            "WHERE f.fromUser = :currentUser " +
            "GROUP BY f.toUser.id, f.toUser.username " +
            "ORDER BY f.toUser.id ASC")
    Page<ResponseFollowingDto> findFollowByFromUser(@Param("currentUser") User currentUser, Pageable pageable);

    @Query("SELECT new com.example.hiddenpiece.domain.dto.user.ResponseFollowerDto(f.fromUser.id, f.fromUser.username, f.createdAt) " +
            "FROM Follow f " +
            "WHERE f.toUser = :currentUser " +
            "GROUP BY f.fromUser.id, f.fromUser.username " +
            "ORDER BY f.fromUser.id ASC")
    Page<ResponseFollowerDto> findFollowByToUser(@Param("currentUser") User currentUser, Pageable pageable);

    @Query("SELECT a FROM Article a " +
            "JOIN a.user u " +
            "JOIN Follow f ON u = f.toUser " +
            "WHERE f.fromUser = :currentUser")
    Page<Article> findArticlesByFromUserFollowing(@Param("currentUser") User currentUser, Pageable pageable);
}
