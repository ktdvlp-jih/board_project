package com.rsp.platform.domain.board.vo;

import com.rsp.platform.domain.board.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardVo {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private Long viewCount;
    private Boolean isDelete;
    private Boolean isEnable;
    private String insertId;
    private LocalDateTime insertDate;
    private String updateId;
    private LocalDateTime updateDate;

    public static BoardVo fromEntity(BoardEntity entity) {
        return BoardVo.builder()
                .boardId(entity.getBoardId())
                .boardTitle(entity.getBoardTitle())
                .boardContent(entity.getBoardContent())
                .viewCount(entity.getViewCount())
                .isDelete(entity.getIsDelete())
                .isEnable(entity.getIsEnable())
                .insertId(entity.getInsertId())
                .insertDate(entity.getInsertDate())
                .updateId(entity.getUpdateId())
                .updateDate(entity.getUpdateDate())
                .build();
    }
}