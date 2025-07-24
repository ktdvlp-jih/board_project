package com.rsp.platform.domain.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rsp.platform.domain.board.dto.BoardListResponse;
import com.rsp.platform.domain.board.entity.QBoardEntity;
import com.rsp.platform.domain.file.dto.AttachFileResponse;
import com.rsp.platform.domain.file.entity.QAttachFileEntity;
import com.rsp.platform.domain.file.entity.QLinkFileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    public Page<BoardListResponse> searchBoards(String codeId, String boardTitle, String boardContent,
                                                LocalDateTime fromDate, LocalDateTime toDate,
                                                int page, int size, String sortBy, String sortDirection) {
        
        QBoardEntity board = QBoardEntity.boardEntity;
        QLinkFileEntity linkFile = QLinkFileEntity.linkFileEntity;

        // 1. 동적 검색 조건 생성
        BooleanBuilder builder = new BooleanBuilder();
        
        // 기본 조건 (삭제되지 않고 활성화된 게시글)
        builder.and(board.isDelete.isFalse())
               .and(board.isEnable.isTrue());
        
        // 동적 검색 조건 추가
        if (codeId != null && !codeId.isBlank()) {
            switch (codeId) {
                case "001": // 제목만 검색
                    if (boardTitle != null && !boardTitle.isBlank()) {
                        builder.and(board.boardTitle.contains(boardTitle));
                    }
                    break;
                case "002": // 내용만 검색
                    if (boardTitle != null && !boardTitle.isBlank()) {
                        builder.and(board.boardContent.contains(boardTitle));
                    }
                    break;
                case "003": // 제목+내용 통합검색
                    if (boardTitle != null && !boardTitle.isBlank()) {
                        builder.and(
                            board.boardTitle.contains(boardTitle)
                            .or(board.boardContent.contains(boardTitle))
                        );
                    }
                    break;
                default:
                    // 기본값: 제목+내용 별도 검색
                    if (boardTitle != null && !boardTitle.isBlank()) {
                        builder.and(board.boardTitle.contains(boardTitle));
                    }
                    if (boardContent != null && !boardContent.isBlank()) {
                        builder.and(board.boardContent.contains(boardContent));
                    }
                    break;
            }
        } else {
            // codeId 없을 때 기본 검색
            if (boardTitle != null && !boardTitle.isBlank()) {
                builder.and(board.boardTitle.contains(boardTitle));
            }
            if (boardContent != null && !boardContent.isBlank()) {
                builder.and(board.boardContent.contains(boardContent));
            }
        }
        

        
        try {
            // 검색시작일자 파싱 (00:00:00)
            String fromYear = String.valueOf(fromDate.getYear());
            String fromMonth = String.valueOf(fromDate.getMonthValue());
            String fromDay = String.valueOf(fromDate.getDayOfMonth());
            java.time.LocalDateTime fromDateTime = java.time.LocalDateTime.of(
                Integer.parseInt(fromYear), Integer.parseInt(fromMonth), Integer.parseInt(fromDay), 0, 0, 0);
            
            // 검색종료일자 파싱 (23:59:59)
            String toYear = String.valueOf(toDate.getYear());
            String toMonth = String.valueOf(toDate.getMonthValue());
            String toDay = String.valueOf(toDate.getDayOfMonth());
            java.time.LocalDateTime toDateTime = java.time.LocalDateTime.of(
                Integer.parseInt(toYear), Integer.parseInt(toMonth), Integer.parseInt(toDay), 23, 59, 59);
            
            builder.and(board.insertDate.between(fromDateTime, toDateTime));
        } catch (Exception e) {
            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다.");
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

    /**
     * 게시글별 첨부파일 목록 조회 (QueryDSL)
     * SQL: SELECT original_filename, file_size, file_path
     *      FROM rsp_link_file a LEFT JOIN rsp_attach_file b ON (b.attach_id = a.attach_file_id)
     *      WHERE a.ref_id = ?
     */
    public List<AttachFileResponse> findFilesByBoardIdAndIsDeleteFalse(Long boardId) {
        QLinkFileEntity linkFile = QLinkFileEntity.linkFileEntity;
        QAttachFileEntity attachFile = QAttachFileEntity.attachFileEntity;

        return queryFactory
                .select(Projections.constructor(AttachFileResponse.class,
                    attachFile.attachId,
                    attachFile.originalFilename,
                    attachFile.saveFilename,
                    attachFile.filePath,
                    attachFile.fileSize
                ))
                .from(linkFile)
                .leftJoin(attachFile).on(attachFile.attachId.eq(linkFile.attachFileId))
                .where(linkFile.refId.eq(boardId))
                .where(linkFile.isDelete.eq(false))
                .orderBy(linkFile.linkFileId.asc())
                .fetch();
    }
}