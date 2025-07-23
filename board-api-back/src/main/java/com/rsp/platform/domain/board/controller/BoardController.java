package com.rsp.platform.domain.board.controller;

import com.rsp.platform.domain.board.dto.BoardDetailResponse;
import com.rsp.platform.domain.board.dto.BoardListResponse;
import com.rsp.platform.domain.board.dto.BoardRequest;
import com.rsp.platform.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 목록 조회 및 검색 (GET /api/boards?codeId=...&boardTitle=...&fromDate=...&toDate=...)
    @GetMapping
    public ResponseEntity<Page<BoardListResponse>> getBoardList(
            @RequestParam(required = false) String codeId,
            @RequestParam(required = false) String boardTitle,
            @RequestParam(required = false) String boardContent,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "boardId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("게시글 목록/검색 요청 - codeId: {}, boardTitle: {}, 검색기간: {} ~ {}", 
                codeId, boardTitle, fromDate, toDate);

        // 서비스 호출 (검색 조건, 페이징, 정렬) - 유효성 검증은 서비스에서 처리
        Page<BoardListResponse> result = boardService.searchBoards(
            codeId, boardTitle, boardContent, fromDate, toDate, 
            page, size, sortBy, sortDirection);

        log.info("조회 완료 - {}건 ({}페이지 중 {}페이지)", 
                result.getTotalElements(), result.getTotalPages(), result.getNumber() + 1);

        return ResponseEntity.ok(result);
    }

    // 게시글 상세 조회 (GET /api/boards/{boardId})
    @GetMapping(value = "/{boardId}")
    public ResponseEntity<BoardDetailResponse> boardInfo(@PathVariable Long boardId) {
        log.info("게시글 상세 요청 - ID: {}", boardId);
        BoardDetailResponse board = boardService.boardInfo(boardId);
        return ResponseEntity.ok(board);
    }

    // 게시글 등록 (POST /api/boards) - 파일 첨부 미지원
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDetailResponse> insertBoard(
            @RequestBody BoardRequest dto ) {
        
        log.info("새 게시글 등록 요청: {}",
                dto.getBoardTitle(),dto.getBoardContent());
        
        // 게시글 저장
        BoardDetailResponse savedBoard = boardService.insertBoard(dto);
        
        return ResponseEntity.ok(savedBoard);
    }

    // 게시글 등록 (POST /api/boards) - 파일 첨부 지원
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardDetailResponse> insertBoardMultipart(
            @RequestPart BoardRequest dto,
            @RequestPart(value = "files", required = false) MultipartFile[] files) throws IOException {

        log.info("새 게시글 등록 요청: {}, 파일 수: {}",
                dto.getBoardTitle(), files != null ? files.length : 0);

        // 게시글 저장
        BoardDetailResponse savedBoard = boardService.insertBoard(dto,files);

        return ResponseEntity.ok(savedBoard);
    }

    // 게시글 수정 (PUT /api/boards/{boardId})
    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardDetailResponse> updateBoard(
            @PathVariable Long boardId,
            @RequestPart BoardRequest dto,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {

        log.info("게시글 수정 요청 -ID:{} {}, 파일 수: {}", boardId, dto.getBoardTitle(), files != null ? files.length : 0);
        BoardDetailResponse response = boardService.updateBoard(boardId, dto, files);
        return ResponseEntity.ok(response);
    }

    // 게시글 삭제 (DELETE /api/boards/{boardId})
    @DeleteMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardDetailResponse> deleteBoard(
            @PathVariable Long boardId,
            @RequestPart BoardRequest dto,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {
        log.info("게시글 삭제 요청 - ID: {}", boardId);
        BoardDetailResponse response = boardService.deleteBoard(boardId, dto, files);

        return ResponseEntity.ok(response);
    }

}
