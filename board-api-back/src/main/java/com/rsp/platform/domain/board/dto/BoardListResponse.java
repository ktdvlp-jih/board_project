package com.rsp.platform.domain.board.dto;

import com.rsp.platform.domain.board.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardListResponse {
    private Long boardId;               // ID
    private String boardTitle;          // 제목
    private Long viewCount;             // 조회수
    private Long fileCount;             // 파일 건수
    private Boolean isFile;             // 첨부파일 유무 (UI 표시용)
    private String insertId;            // 작성자
    private LocalDateTime insertDate;   // 작성일자

    /*
    * 목록 : 제목, 첨부파일 유무, 등록일시, 조회수, 작성자
    * */
    public static BoardListResponse fromEntity(BoardEntity entity) {
        return BoardListResponse.builder()
                .boardId(entity.getBoardId())
                .boardTitle(entity.getBoardTitle())
                .viewCount(entity.getViewCount())
                .insertId(entity.getInsertId())
                .insertDate(entity.getInsertDate())
                .build();
    }

    // QueryDSL 전용 생성자 (hasAttachment 제외, 6개 파라미터)
    public BoardListResponse(Long boardId, String boardTitle, Long viewCount, 
                           Long fileCount, String insertId, LocalDateTime insertDate) {
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.viewCount = viewCount;
        this.fileCount = fileCount;
        this.isFile = (fileCount != null && fileCount > 0); // 자동 계산
        this.insertId = insertId;
        this.insertDate = insertDate;
    }

    // fileCount 기반으로 isFile 자동 설정
    public void setFileCount(Long fileCount) {
        this.fileCount = fileCount;
        this.isFile = (fileCount != null && fileCount > 0);
    }

}
