package com.rsp.platform.domain.board.controller;

import com.rsp.platform.domain.board.dto.BoardRequest;
import com.rsp.platform.domain.board.entity.BoardEntity;
import com.rsp.platform.domain.board.service.BoardService;
import com.rsp.platform.domain.board.vo.BoardVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 목록 조회 (GET /api/boards?title=...&author=...)
    @GetMapping
    public ResponseEntity<?> getBoardList(
            @RequestParam(required = false) String boardTitle,
            @RequestParam(required = false) String insertId) {

        log.info("게시글 목록 요청 - 제목: {}, 작성자: {}", boardTitle, insertId);
        List<BoardEntity> result = boardService.getBoardList(boardTitle, insertId);

        if (result.isEmpty()) {
            return ResponseEntity.ok().body("{\"message\":\"게시글이 없습니다.\"}");
        }

        return ResponseEntity.ok(result);
    }

    // 게시글 상세 조회 (GET /api/boards/{boardId})
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardVo> boardInfo(@PathVariable Long boardId) {
        log.info("게시글 상세 요청 - ID: {}", boardId);
        BoardVo board = boardService.boardInfo(boardId);
        return ResponseEntity.ok(board);
    }

    // 게시글 등록 (POST /api/boards)
    @PostMapping
    public ResponseEntity<BoardRequest> insertBoard(@RequestBody BoardRequest dto) {
        log.info("새 게시글 등록 요청: {}", dto.getBoardTitle());
        return ResponseEntity.ok(boardService.insertBoard(dto));
    }

    // 게시글 수정 (PUT /api/boards/{boardId})
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardRequest> updateBoard(@PathVariable Long boardId, @RequestBody BoardRequest dto) {
        log.info("게시글 수정 요청 - ID: {}", boardId);
        return ResponseEntity.ok(boardService.updateBoard(boardId, dto));
    }



    // 게시글 삭제 (DELETE /api/boards/{boardId})
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId, @RequestBody BoardRequest dto) {
        log.info("게시글 삭제 요청 - ID: {}", boardId);
        boardService.deleteBoard(boardId,dto);
        return ResponseEntity.ok().body("{\"message\":\"삭제되었습니다.\"}");
    }

}
