package com.rsp.board.repository;

import com.rsp.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
        쿼리메소드 작성방법
        findBy	: 조회 시작 키워드
        필드명	: 엔티티 클래스의 필드 이름 (카멜케이스 기준)
        And, Or	: 조건 연결
        OrderBy	: 정렬 키워드
        Desc, Asc	정렬 방향

        | 메서드 이름                                            | 자동 생성 SQL                                   |
        | ------------------------------------------------- | ------------------------------------------- |
        | `findByBoardTitle(String title)`                  | `WHERE board_title = ?`                     |
        | `findByInsertIdAndIsDeleteFalse(String insertId)` | `WHERE insert_id = ? AND is_delete = false` |
        | `findTop5ByOrderByInsertDateDesc()`               | `ORDER BY insert_date DESC LIMIT 5`         |


 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByIsDeleteFalseAndIsEnableTrueOrderByBoardIdDesc();
}
