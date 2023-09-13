package com.example.hiddenpiece.domain.repository.community;

import com.example.hiddenpiece.domain.entity.community.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
