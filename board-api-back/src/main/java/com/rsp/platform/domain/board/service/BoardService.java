package com.rsp.platform.domain.board.service;

import com.rsp.platform.domain.board.dto.BoardRequest;
import com.rsp.platform.domain.board.entity.BoardEntity;
import com.rsp.platform.common.exception.NoContentException;
import com.rsp.platform.domain.board.repository.BoardRepository;
import com.rsp.platform.domain.board.vo.BoardVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import jakarta.persistence.criteria.Predicate;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;


    public List<BoardEntity> getBoardList(String boardTitle, String insertId) {
        // 방법 1: Specification 방식 (동적 조건)
        List<BoardEntity> result = boardRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (boardTitle != null && !boardTitle.isBlank()) {
                predicates.add(cb.like(root.get("boardTitle"), "%" + boardTitle + "%"));
            }
            if (insertId != null && !insertId.isBlank()) {
                predicates.add(cb.equal(root.get("insertId"), insertId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });

        if (result.isEmpty()) {
            throw new NoContentException("내용이 없습니다.");
        }

        return result;

        /*
        // 방법 2: 메서드 네이밍 방식 (단순 조건)
        if (boardTitle != null && insertId != null) {
            return boardRepository.findByBoardTitleContainingAndInsertIdOrderByIdDesc(boardTitle, insertId);
        }
        return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        */
    }

    public BoardVo boardInfo(Long boardId) {
        BoardEntity entity = boardRepository.findByBoardIdAndIsDeleteFalse(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));

        return BoardVo.fromEntity(entity);
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
        BoardEntity entity = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다."));

        // setter 없는 스타일이면 엔티티에 비즈니스 메서드로 변경
        entity.update(dto.getBoardTitle(), dto.getBoardContent(), dto.getInsertId());
        BoardEntity updated = boardRepository.save(entity);

        // 엔티티 → DTO 변환
        return BoardRequest.fromEntity(updated);
    }

    // 게시글 삭제 (소프트 딜리트)
    public BoardRequest deleteBoard(Long boardId, BoardRequest dto) {
        BoardEntity entity = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다."));

        // 삭제된 게시글인지 확인
        if (entity.getIsDelete()) {
            throw new IllegalStateException("이미 삭제된 게시글입니다.");
        }

        // 소프트 딜리트 수행
        entity.delete(dto.getUpdateId());

        BoardEntity updated = boardRepository.save(entity);

        // 엔티티 → DTO 변환
        return BoardRequest.fromEntity(updated);
    }




}
