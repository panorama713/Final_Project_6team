package com.example.hiddenpiece.domain.repository.user;

import com.example.hiddenpiece.domain.entity.user.Question;
import com.example.hiddenpiece.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByRealNameAndEmail(String realName, String email);

    boolean existsByUsernameAndRealName(String username, String realName);

    boolean existsByRealNameAndEmailAndQuestionAndAnswer(String realName, String email, Question question, String answer);

    @Query("SELECT u.username FROM User u WHERE u.realName = :realName AND u.email = :email AND u.question = :question AND u.answer = :answer")
    String findUsernameByRealNameAndEmailAndQuestionAndAnswer(@Param("realName") String realName, @Param("email") String email, @Param("question") Question question, @Param("answer") String answer);

    @Query("SELECT u.id FROM User u WHERE u.realName = :realName AND u.username = :username")
    Long existUserByRealNameAndUsername(@Param("realName") String realName, @Param("username") String username);

    @Query(value = "SELECT COUNT(*) FROM user WHERE username = :username AND deleted_at IS NOT NULL", nativeQuery = true)
    Integer countByUsernameAndDeleted(@Param("username") String username);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM user WHERE email = :email AND deleted_at IS NOT NULL", nativeQuery = true)
    Integer countByEmailAndDeleted(@Param("email") String email);
}
