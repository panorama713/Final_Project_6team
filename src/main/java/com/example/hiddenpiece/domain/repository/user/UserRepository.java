package com.example.hiddenpiece.domain.repository.user;

import com.example.hiddenpiece.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT u.username FROM User u WHERE u.realName = :realName AND u.email = :email")
    String findUsernameByRealNameAndEmail(@Param("realName") String realName, @Param("email") String email);

    @Query("SELECT u.id FROM User u WHERE u.realName = :realName AND u.username = :username")
    Long existUserByRealNameAndUsername(@Param("realName") String realName, @Param("username") String username);
}
