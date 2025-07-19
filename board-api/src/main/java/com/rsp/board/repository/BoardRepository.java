package com.rsp.board.repository;

import com.rsp.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByIsDeleteFalseAndIsEnableTrueOrderByBoardIdDesc();
}
