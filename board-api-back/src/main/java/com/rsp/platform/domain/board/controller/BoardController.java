package com.rsp.platform.domain.board.controller;

import com.rsp.platform.domain.board.dto.BoardRequest;
import com.rsp.platform.domain.board.entity.BoardEntity;
import com.rsp.platform.domain.board.service.BoardService;
import com.rsp.platform.domain.board.vo.BoardVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 목록 조회 및 검색 (GET /api/boards?title=...&author=... 또는 GET /api/boards)
    @GetMapping
    public ResponseEntity<Page<BoardEntity>> getBoardList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String boardTitle,
            @RequestParam(required = false) String boardContent,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Long minViewCount,
            @RequestParam(required = false) Long maxViewCount,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "boardId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("게시글 목록/검색 요청 - 키워드: {}, 제목: {}, 작성자: {}", keyword, boardTitle, author);
        
        Page<BoardEntity> result = boardService.searchBoards(
                keyword, boardTitle, boardContent, author, 
                minViewCount, maxViewCount, startDate, endDate,
                page, size, sortBy, sortDirection
        );

        log.info("조회 완료 - {}건 ({}페이지 중 {}페이지)", 
                result.getTotalElements(), result.getTotalPages(), result.getNumber() + 1);

        return ResponseEntity.ok(result);
    }

    // 게시글 상세 조회 (GET /api/boards/{boardId})
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardVo> boardInfo(@PathVariable Long boardId) {
        log.info("게시글 상세 요청 - ID: {}", boardId);
        BoardVo board = boardService.boardInfo(boardId);
        return ResponseEntity.ok(board);
    }

    // 게시글 등록 (POST /api/boards) - 파일 첨부 지원
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardRequest> insertBoard(
            @RequestPart("board") BoardRequest dto,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {
        
        log.info("새 게시글 등록 요청: {}, 파일 수: {}", 
                dto.getBoardTitle(), files != null ? files.length : 0);
        
        // 게시글 저장
        BoardRequest savedBoard = boardService.insertBoard(dto);
        
        return ResponseEntity.ok(savedBoard);
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
