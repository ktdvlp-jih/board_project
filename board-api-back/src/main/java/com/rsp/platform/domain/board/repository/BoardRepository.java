package com.rsp.platform.domain.board.repository;

import com.rsp.platform.domain.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

        | 구분                        | 설명                         | 장점                   | 단점         |
        | -------------------------- | ------------------           | -------------         | ---------- |
        | `JpaSpecificationExecutor` | 조건을 조립해서 유연한 검색 가능  | 조건 조합 많을수록 유리  | 코드 길어짐     |
        | 메서드 네이밍 방식            | 단순한 조건에서 매우 편리        | 코드 짧고 간단          | 조건 많아지면 불리 |

        */

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long>, JpaSpecificationExecutor<BoardEntity> {


    // 게시글 상세 조회 (삭제되지 않은 게시글만)
    Optional<BoardEntity> findByBoardId(Long boardId);
    
    // 게시글 상세 조회 (삭제되지 않고 활성화된 게시글만)
    Optional<BoardEntity> findByBoardIdAndIsDeleteFalseAndIsEnableTrue(Long boardId);



}

/*
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findByBoardTitleContainingAndInsertIdContainingAndIsDeleteFalseOrderByBoardIdDesc(String boardTitle, String insertId);
}
*/