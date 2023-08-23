package com.example.hiddenpiece.domain.repository.follow;

import com.example.hiddenpiece.domain.entity.follow.Follow;
import com.example.hiddenpiece.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByToUserAndFromUser(User toUser, User fromUser);

    boolean existsByToUserAndFromUser(User toUser, User fromUser);

    int countByFromUser(User fromUser);
    int countByToUser(User toUser);
}
