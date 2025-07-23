package com.rsp.platform.domain.board.dto;

import com.rsp.platform.domain.board.entity.BoardEntity;
import com.rsp.platform.domain.file.dto.AttachFileResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDetailResponse {

    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private Long viewCount;
    private LocalDateTime startDate;    // 공지 시작일
    private LocalDateTime endDate;      // 공지 종료일
    private Boolean isDelete;
    private Boolean isEnable;
    private String insertId;
    private LocalDateTime insertDate;
    private String updateId;
    private LocalDateTime updateDate;
    private List<AttachFileResponse> files;

    public static BoardDetailResponse fromEntity(BoardEntity entity) {
        return BoardDetailResponse.builder()
                .boardId(entity.getBoardId())
                .boardTitle(entity.getBoardTitle())
                .boardContent(entity.getBoardContent())
                .viewCount(entity.getViewCount())
                .isDelete(entity.getIsDelete())
                .isEnable(entity.getIsEnable())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                //.files(entity.getLinkFiles())
                .insertId(entity.getInsertId())
                .insertDate(entity.getInsertDate())
                .updateId(entity.getUpdateId())
                .updateDate(entity.getUpdateDate())
                .build();
    }

}
