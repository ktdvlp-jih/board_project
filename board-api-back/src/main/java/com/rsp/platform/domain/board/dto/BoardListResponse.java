package com.rsp.platform.domain.board.dto;

import com.rsp.platform.domain.board.entity.BoardEntity;
import com.rsp.platform.domain.file.dto.AttachFileResponse;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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

}
