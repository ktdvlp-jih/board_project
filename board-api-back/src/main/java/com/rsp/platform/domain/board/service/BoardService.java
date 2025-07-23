package com.rsp.platform.domain.board.service;

import com.rsp.platform.domain.board.dto.BoardDetailResponse;
import com.rsp.platform.domain.board.dto.BoardListResponse;
import com.rsp.platform.domain.board.dto.BoardRequest;
import com.rsp.platform.domain.board.entity.BoardEntity;
import com.rsp.platform.domain.board.repository.BoardRepository;
import com.rsp.platform.domain.board.repository.BoardQueryRepository;
import com.rsp.platform.domain.file.repository.AttachFileRepository;
import com.rsp.platform.domain.file.repository.LinkFileRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.web.multipart.MultipartFile;
import com.rsp.platform.domain.file.entity.AttachFileEntity;
import com.rsp.platform.domain.file.entity.LinkFileEntity;
import com.rsp.platform.common.file.FileStorageService;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileStorageService fileStorageService;
    private final AttachFileRepository attachFileRepository;
    private final LinkFileRepository linkFileRepository;
    private final BoardQueryRepository boardQueryRepository;

    /**
     * 게시글 조회
     */
    public BoardDetailResponse boardInfo(Long boardId) {
        // 400 Bad Request: 잘못된 파라미터
        if (boardId == null || boardId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
        }

        // 404 Not Found: 게시글 없음
        BoardEntity entity = boardRepository.findByBoardIdAndIsDeleteFalseAndIsEnableTrue(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 존재하지 않거나 비활성화되었습니다."));

        return BoardDetailResponse.fromEntity(entity);
    }

    /**
     * 게시글 파일 등록 [미첨부]
     */
    public BoardDetailResponse insertBoard(BoardRequest dto) {

        // 400 Bad Request: 잘못된 파라미터
        if (dto.getBoardTitle() == null || dto.getBoardTitle().isBlank()
                || dto.getBoardContent() == null || dto.getBoardContent().isBlank()
                || dto.getInsertId() == null || dto.getInsertId().isBlank()) {
            throw new IllegalArgumentException("필수 입력값(boardTitle, boardContent, insertId)이 누락되었거나 비어있습니다.");
        }

        // 게시글 엔티티 생성
        BoardEntity board = BoardEntity.create(
                dto.getBoardTitle(),
                dto.getBoardContent(),
                dto.getInsertId()
        );
        BoardEntity saved = boardRepository.save(board);
        return BoardDetailResponse.fromEntity(saved);
    }

    /**
     * 게시글 파일 등록 [첨부]
     */
    public BoardDetailResponse insertBoard(BoardRequest dto, MultipartFile[] files) throws IOException {

        // 400 Bad Request: 잘못된 파라미터
        if (dto.getBoardTitle() == null || dto.getBoardTitle().isBlank()
                || dto.getBoardContent() == null || dto.getBoardContent().isBlank()
                || dto.getInsertId() == null || dto.getInsertId().isBlank()) {
            throw new IllegalArgumentException("필수 입력값(boardTitle, boardContent, insertId)이 누락되었거나 비어있습니다.");
        }

        // 게시글 엔티티 생성
        BoardEntity board = BoardEntity.create(
                dto.getBoardTitle(),
                dto.getBoardContent(),
                dto.getInsertId()
        );

        // 1. 게시글 먼저 저장 (PK 생성)
        BoardEntity savedBoard = boardRepository.save(board);

        // 2. 파일 처리
        // 첨부파일 처리
        if (files != null && files.length > 0) {
            int order = 0;
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // 파일 저장 경로/이름 생성 및 실제 저장
                    String saveFileName = fileStorageService.generateSaveName(file.getOriginalFilename());
                    String filePath = fileStorageService.save(file, saveFileName);

                    // rsp_attach_file 저장 (공통 파일 테이블)
                    AttachFileEntity attachFile = AttachFileEntity.builder()
                            //.boardId(savedBoard.getBoardId())    // 게시글 PK로 연결!
                            .originalFilename(file.getOriginalFilename())
                            .saveFilename(saveFileName)
                            .filePath(filePath)
                            .fileSize(file.getSize())
                            .insertId(dto.getInsertId())
                            .build();
                    System.out.println("attachFileId: " + attachFile.getAttachId()); // save() 직전
                    AttachFileEntity savedAttachFile = attachFileRepository.save(attachFile);

                    // rsp_link_file 저장 (게시글-파일 연결)
                    LinkFileEntity linkFile = LinkFileEntity.builder()
                            .refId(savedBoard.getBoardId())  // 게시글 PK
                            .attachFileId(savedAttachFile.getAttachId()) // 파일 PK
                            .fileOrder(order++)
                            .insertId(dto.getInsertId())
                            .build();
                    linkFileRepository.save(linkFile);
                }
            }
        }

        return BoardDetailResponse.fromEntity(board);
    }

    // 게시글 수정
    public BoardDetailResponse updateBoard(Long boardId, BoardRequest dto, MultipartFile[] files) {
        BoardEntity entity = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없거나 수정할 수 없습니다."));

        // 게시글 내용 수정
        entity.update(dto.getBoardTitle(), dto.getBoardContent(), dto.getUpdateId());

        // 다중 파일 처리 예시
        if (files != null && files.length > 0) {
            // 1. 기존 파일 삭제(옵션)
            // 2. 신규 파일 업로드(for문 돌면서 하나씩 저장)
            // 3. 파일정보 엔티티에 매핑/연결
            for (MultipartFile file : files) {
                // 파일 저장/엔티티 생성/연결 (직접 구현 필요)
                // ex: boardFileService.saveFile(entity, file);
            }
        }

        // JPA는 트랜잭션 내 변경감지, save 생략 가능
        //BoardEntity updated = boardRepository.save(entity);

        // 엔티티 → DTO 변환
        return BoardDetailResponse.fromEntity(entity);
    }

    // 게시글 삭제 (소프트 딜리트)
    public BoardDetailResponse deleteBoard(Long boardId, BoardRequest dto, MultipartFile[] files) {
        BoardEntity entity = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없거나 이미 삭제되었습니다."));

        // 소프트 딜리트 수행
        entity.delete(dto.getUpdateId());

        //BoardEntity updated = boardRepository.save(entity);

        // 엔티티 → DTO 변환
        return BoardDetailResponse.fromEntity(entity);
    }

    // ===== 통합검색 관련 메서드 =====
    

    /**
     * 다중필드 통합검색 (QueryDSL 버전 - 파일 개수 포함)
     */
    public Page<BoardListResponse> searchBoards( String codeId, String boardTitle, String boardContent, String fromDate, String toDate, int page, int size, String sortBy, String sortDirection ) {
        
        // fromDate, toDate 유효성 검증
        validateDateParameters(fromDate, toDate);
        
        return boardQueryRepository.searchBoards(codeId, boardTitle, boardContent, fromDate, toDate, page, size, sortBy, sortDirection);
    }
    
    /**
     * 날짜 파라미터 유효성 검증
     */
    private void validateDateParameters(String fromDate, String toDate) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd");
        
        // 필수값 체크
        if (fromDate == null || fromDate.isBlank()) {
            throw new IllegalArgumentException("시작일자(fromDate)는 필수값입니다. 형식: YYYYMMDD");
        }
        if (toDate == null || toDate.isBlank()) {
            throw new IllegalArgumentException("종료일자(toDate)는 필수값입니다. 형식: YYYYMMDD");
        }
        
        // 길이 체크 (8자리)
        if (fromDate.length() != 8) {
            throw new IllegalArgumentException("시작일자(fromDate) 형식이 올바르지 않습니다. 형식: YYYYMMDD");
        }
        if (toDate.length() != 8) {
            throw new IllegalArgumentException("종료일자(toDate) 형식이 올바르지 않습니다. 형식: YYYYMMDD");
        }
        
        // 숫자 형식 및 유효한 날짜 체크
        try {
            java.time.LocalDate fromLocalDate = java.time.LocalDate.parse(fromDate, formatter);
            java.time.LocalDate toLocalDate = java.time.LocalDate.parse(toDate, formatter);
            
            // 날짜 범위 체크 (시작일이 종료일보다 늦으면 안됨)
            if (fromLocalDate.isAfter(toLocalDate)) {
                throw new IllegalArgumentException("시작일자가 종료일자보다 늦을 수 없습니다.");
            }
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("유효하지 않은 날짜입니다. 올바른 형식: YYYYMMDD (예: 20250724)");
        }
    }





}
