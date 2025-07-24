package com.rsp.platform.domain.board.service;

import com.rsp.platform.common.util.CommonUtils;
import com.rsp.platform.domain.board.dto.BoardDetailResponse;
import com.rsp.platform.domain.board.dto.BoardListResponse;
import com.rsp.platform.domain.board.dto.BoardRequest;
import com.rsp.platform.domain.board.entity.BoardEntity;
import com.rsp.platform.domain.board.repository.BoardRepository;
import com.rsp.platform.domain.board.repository.BoardQueryRepository;
import com.rsp.platform.domain.file.dto.AttachFileResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.web.multipart.MultipartFile;
import com.rsp.platform.domain.file.entity.AttachFileEntity;
import com.rsp.platform.domain.file.entity.LinkFileEntity;
import com.rsp.platform.common.file.FileStorageService;

import static com.rsp.platform.common.util.CommonUtils.validateDateParameters;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileStorageService fileStorageService;
    private final AttachFileRepository attachFileRepository;
    private final LinkFileRepository linkFileRepository;
    private final BoardQueryRepository boardQueryRepository;
    //private final CommonUtils commonUtils;



    /**
     * 게시글 조회 (첨부파일 포함)
     */
    public BoardDetailResponse boardInfo(Long boardId) {
        // 400 Bad Request: 잘못된 파라미터
        if (boardId == null || boardId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
        }

        // 404 Not Found: 게시글 없음
        BoardEntity entity = boardRepository.findByBoardIdAndIsDeleteFalseAndIsEnableTrue(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 존재하지 않거나 비활성화되었습니다."));

        // 조회수 증가
        entity.incrementViewCount();
        boardRepository.save(entity);

        // 첨부파일 목록 조회
        List<AttachFileResponse> files = boardQueryRepository.findFilesByBoardId(boardId);

        // BoardDetailResponse 생성 후 파일 목록 설정
        BoardDetailResponse response = BoardDetailResponse.fromEntity(entity);
        response.setFiles(files);

        return response;
    }

    /**
     * 게시글 파일 등록 [미첨부]
     */
    @Transactional
    public BoardDetailResponse insertBoard(BoardRequest dto) {

        // 400 Bad Request: 잘못된 파라미터
        if (dto.getBoardTitle() == null || dto.getBoardTitle().isBlank()
                || dto.getBoardContent() == null || dto.getBoardContent().isBlank()
                || dto.getStartDate() == null
                || dto.getEndDate() == null
                || dto.getInsertId() == null || dto.getInsertId().isBlank()) {
            throw new IllegalArgumentException("필수 입력값(boardTitle, boardContent, StartDate, EndDate, insertId)이 누락되었거나 비어있습니다.");
        }

        validateDateParameters(dto.getStartDate(), dto.getEndDate());

        // 게시글 엔티티 생성
        BoardEntity board = BoardEntity.create(
                dto.getBoardTitle(),
                dto.getBoardContent(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getInsertId()
        );
        BoardEntity saved = boardRepository.save(board);
        return BoardDetailResponse.fromEntity(saved);
    }

    /**
     * 게시글 파일 등록 [첨부]
     */
    @Transactional
    public BoardDetailResponse insertBoard(BoardRequest dto, MultipartFile[] files) throws IOException {

        // 400 Bad Request: 잘못된 파라미터
        if (dto.getBoardTitle() == null || dto.getBoardTitle().isBlank()
                || dto.getBoardContent() == null || dto.getBoardContent().isBlank()
                || dto.getStartDate() == null
                || dto.getEndDate() == null
                || dto.getInsertId() == null || dto.getInsertId().isBlank()) {
            throw new IllegalArgumentException("필수 입력값(boardTitle, boardContent, StartDate, EndDate, insertId)이 누락되었거나 비어있습니다.");
        }

        // 게시글 엔티티 생성
        BoardEntity board = BoardEntity.create(
                dto.getBoardTitle(),
                dto.getBoardContent(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getInsertId()
        );

        // 1. 게시글 먼저 저장 (PK 생성)
        BoardEntity savedBoard = boardRepository.save(board);

        // 2. 파일 처리
        // 첨부파일 처리
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // 파일 저장 경로/이름 생성 및 실제 저장
                    String saveFileName = fileStorageService.generateSaveName(file.getOriginalFilename());
                    String filePath = fileStorageService.save(file, saveFileName);

                    // rsp_attach_file 저장 (공통 파일 테이블)
                    AttachFileEntity attachFile = AttachFileEntity.builder()
                            // attachId는 자동 생성되므로 설정하지 않음 (동시성 문제 해결)
                            .originalFilename(file.getOriginalFilename())
                            .saveFilename(saveFileName)
                            .filePath(filePath)
                            .fileSize(file.getSize())
                            .insertId(dto.getInsertId())
                            .build();
                    AttachFileEntity savedAttachFile = attachFileRepository.save(attachFile);

                    // rsp_link_file 저장 (게시글-파일 연결)
                    LinkFileEntity linkFile = LinkFileEntity.builder()
                            .refId(savedBoard.getBoardId())  // 게시글 PK
                            .attachFileId(savedAttachFile.getAttachId()) // 파일 PK
                            .insertId(dto.getInsertId())
                            .build();
                    linkFileRepository.save(linkFile);
                }
            }
        }

        return BoardDetailResponse.fromEntity(board);
    }

    /**
     * 게시글 수정 (첨부파일 포함)
     * 기존 파일은 소프트 삭제 후 새 파일로 교체
     * @Transactional: JPA 변경감지를 통한 자동 UPDATE 처리
     */
    @Transactional
    public BoardDetailResponse updateBoard(Long boardId, BoardRequest dto, MultipartFile[] files) throws IOException {

        // 404 Not Found: 게시글 없음 (활성화된 게시글만 수정 가능)
        BoardEntity boardEntity = boardRepository.findByBoardIdAndIsDeleteFalseAndIsEnableTrue(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없거나 수정할 수 없습니다."));

        // 400 Bad Request: 필수 파라미터 검증
        if (dto.getBoardTitle() == null || dto.getBoardTitle().isBlank()
                || dto.getBoardContent() == null || dto.getBoardContent().isBlank()
                || dto.getUpdateId() == null || dto.getUpdateId().isBlank()) {
            throw new IllegalArgumentException("필수 입력값(boardTitle, boardContent, updateId)이 누락되었거나 비어있습니다.");
        }

        // 1. 게시글 내용 수정 (JPA 변경감지로 자동 업데이트 - save() 불필요)
        boardEntity.update(dto.getBoardTitle(), dto.getBoardContent(), dto.getUpdateId());

        // 2. 기존 첨부파일 소프트 삭제 처리
        List<LinkFileEntity> existingLinkFiles = linkFileRepository.findByRefId(boardId);
        for (LinkFileEntity linkFile : existingLinkFiles) {
            linkFile.update(boardId, dto.getUpdateId());
            linkFileRepository.save(linkFile); // DB 반영 필수!
        }

        // 3. 새로운 첨부파일 등록 처리
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // 3-1. 실제 파일 저장 (UUID 기반 고유명 생성)
                    String saveFileName = fileStorageService.generateSaveName(file.getOriginalFilename());
                    String filePath = fileStorageService.save(file, saveFileName);

                    // 3-2. rsp_attach_file 테이블에 파일 메타정보 저장
                    AttachFileEntity attachFile = AttachFileEntity.builder()
                            .originalFilename(file.getOriginalFilename())
                            .saveFilename(saveFileName)
                            .filePath(filePath)
                            .fileSize(file.getSize())
                            .insertId(dto.getUpdateId()) // 수정 시에는 updateId 사용
                            .build();
                    AttachFileEntity savedAttachFile = attachFileRepository.save(attachFile);

                    // 3-3. rsp_link_file 테이블에 게시글-파일 연결 관계 저장
                    LinkFileEntity newLinkFile = LinkFileEntity.builder()
                            .refId(boardId)  // 게시글 ID
                            .attachFileId(savedAttachFile.getAttachId()) // 방금 저장한 파일 ID
                            .insertId(dto.getUpdateId()) // 수정 시에는 updateId 사용
                            .build();
                    linkFileRepository.save(newLinkFile);
                }
            }
        }

        // 4. 수정된 게시글 정보 반환 (첨부파일 목록 포함)
        BoardDetailResponse response = BoardDetailResponse.fromEntity(boardEntity);
        List<AttachFileResponse> updatedFiles = boardQueryRepository.findFilesByBoardId(boardId);
        response.setFiles(updatedFiles);
        
        return response;
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
    public Page<BoardListResponse> searchBoards( String codeId, String boardTitle, String boardContent, LocalDateTime fromDate,LocalDateTime toDate, int page, int size, String sortBy, String sortDirection ) {
        
        // fromDate, toDate 유효성 검증 사용 X
        //validateDateParameters(fromDate, toDate);
        
        return boardQueryRepository.searchBoards(codeId, boardTitle, boardContent, fromDate, toDate, page, size, sortBy, sortDirection);
    }
}
