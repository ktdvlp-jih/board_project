package com.rsp.platform.domain.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rsp.platform.domain.board.dto.BoardListResponse;
import com.rsp.platform.domain.board.entity.QBoardEntity;
import com.rsp.platform.domain.file.entity.QLinkFileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 게시글 검색 with 파일 개수 (QueryDSL)
     * SQL: SELECT board_id, board_title, view_count, COUNT(attach_file_id) as file_count, insert_id, insert_date
     *      FROM rsp_board a INNER JOIN rsp_link_file b ON (b.ref_id = a.board_id)
     *      GROUP BY board_id, board_title, view_count, insert_id, insert_date
     */
    public Page<BoardListResponse> searchBoards(String boardTitle, String boardContent, 
                                               int page, int size, String sortBy, String sortDirection) {
        
        QBoardEntity board = QBoardEntity.boardEntity;
        QLinkFileEntity linkFile = QLinkFileEntity.linkFileEntity;

        // 1. 동적 검색 조건 생성
        BooleanBuilder builder = new BooleanBuilder();
        
        // 기본 조건 (삭제되지 않고 활성화된 게시글)
        builder.and(board.isDelete.isFalse())
               .and(board.isEnable.isTrue());
        
        // 동적 검색 조건 추가
        if (boardTitle != null && !boardTitle.isBlank()) {
            builder.and(board.boardTitle.containsIgnoreCase(boardTitle));
        }
        if (boardContent != null && !boardContent.isBlank()) {
            builder.and(board.boardContent.containsIgnoreCase(boardContent));
        }
        
        // 2. 정렬 설정
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection).orElse(Sort.Direction.DESC);
        
        // 3. 파일 개수 포함 QueryDSL 쿼리 실행
        List<BoardListResponse> content = queryFactory
                .select(Projections.constructor(BoardListResponse.class,
                    board.boardId,
                    board.boardTitle,
                    board.viewCount,
                    linkFile.attachFileId.count().as("fileCount"),
                    board.insertId,
                    board.insertDate
                ))
                .from(board)
                .innerJoin(linkFile).on(linkFile.refId.eq(board.boardId))
                .where(builder)
                .groupBy(board.boardId, board.boardTitle, board.viewCount, board.insertId, board.insertDate)
                .orderBy(direction == Sort.Direction.ASC ? 
                    board.boardId.asc() : board.boardId.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();

        // 4. 전체 카운트 조회
        Long total = queryFactory
                .select(board.boardId.countDistinct())
                .from(board)
                .innerJoin(linkFile).on(linkFile.refId.eq(board.boardId))
                .where(builder)
                .fetchOne();
        
        // 5. Page 객체 생성
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }
}