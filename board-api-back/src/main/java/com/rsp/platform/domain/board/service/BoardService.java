package com.rsp.platform.domain.board.service;

import com.rsp.platform.domain.board.dto.BoardRequest;
import com.rsp.platform.domain.board.entity.BoardEntity;
import com.rsp.platform.common.exception.NoContentException;
import com.rsp.platform.domain.board.repository.BoardRepository;
import com.rsp.platform.domain.board.vo.BoardVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;



    public BoardVo boardInfo(Long boardId) {
        BoardEntity entity = boardRepository.findByBoardIdAndIsDeleteFalseAndIsEnableTrue(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않거나 비활성화되었습니다."));

        return BoardVo.fromEntity(entity);
    }

    /**
     * 게시글 Entity 조회 (파일 업로드용)
     */
    public BoardEntity findEntityById(Long boardId) {
        return boardRepository.findByBoardIdAndIsDeleteFalseAndIsEnableTrue(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));
    }


    public BoardRequest insertBoard(BoardRequest dto) {
        BoardEntity board = BoardEntity.create(
                dto.getBoardTitle(),
                dto.getBoardContent(),
                dto.getInsertId()
        );
        BoardEntity saved = boardRepository.save(board);
        return BoardRequest.fromEntity(saved);
    }

    // 게시글 수정
    public BoardRequest updateBoard(Long boardId, BoardRequest dto) {
        BoardEntity entity = boardRepository.findByBoardIdAndIsDeleteFalseAndIsEnableTrue(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없거나 수정할 수 없습니다."));

        // 게시글 내용 수정
        entity.update(dto.getBoardTitle(), dto.getBoardContent(), dto.getUpdateId());
        BoardEntity updated = boardRepository.save(entity);

        // 엔티티 → DTO 변환
        return BoardRequest.fromEntity(updated);
    }

    // 게시글 삭제 (소프트 딜리트)
    public BoardRequest deleteBoard(Long boardId, BoardRequest dto) {
        BoardEntity entity = boardRepository.findByBoardIdAndIsDeleteFalseAndIsEnableTrue(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없거나 이미 삭제되었습니다."));

        // 소프트 딜리트 수행
        entity.delete(dto.getUpdateId());

        BoardEntity updated = boardRepository.save(entity);

        // 엔티티 → DTO 변환
        return BoardRequest.fromEntity(updated);
    }

    // ===== 통합검색 관련 메서드 =====
    
    
    
    
    
    
    /**
     * 다중필드 통합검색
     */
    public Page<BoardEntity> searchBoards(String keyword, String boardTitle, String boardContent, 
                                        String author, Long minViewCount, Long maxViewCount, 
                                        LocalDateTime startDate, LocalDateTime endDate,
                                        int page, int size, String sortBy, String sortDirection) {
        
        // 정렬 조건 생성
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
        
        String validSortBy = switch (sortBy) {
            case "boardTitle", "insertDate", "updateDate", "viewCount" -> sortBy;
            default -> "boardId";
        };
        
        Sort sort = Sort.by(direction, validSortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Repository 호출
        return boardRepository.searchBoards(
                keyword, boardTitle, boardContent, author,
                minViewCount, maxViewCount, startDate, endDate, pageable
        );
    }
    




}
