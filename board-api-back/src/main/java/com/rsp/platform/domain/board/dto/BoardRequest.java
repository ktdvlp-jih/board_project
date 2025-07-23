package com.rsp.platform.domain.board.dto;

import com.rsp.platform.domain.board.entity.BoardEntity;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequest {

    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private Long viewCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isDelete;
    private Boolean isEnable;
    private String insertId;
    private LocalDateTime insertDate;
    private String updateId;
    private LocalDateTime updateDate;
    //private MultipartFile[] getFiles;





    public static BoardRequest fromEntity(BoardEntity entity) {
        return BoardRequest.builder()
                .boardId(entity.getBoardId())
                .boardTitle(entity.getBoardTitle())
                .boardContent(entity.getBoardContent())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .insertId(entity.getInsertId())
                .updateId(entity.getUpdateId())
                .viewCount(entity.getViewCount())
                .build();
    }


}


