package com.rsp.platform.domain.board.dto;

import com.rsp.platform.domain.board.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponse {

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

    public static BoardResponse fromEntity(BoardEntity entity) {
        return BoardResponse.builder()
                .boardId(entity.getBoardId())
                .boardTitle(entity.getBoardTitle())
                .boardContent(entity.getBoardContent())
                .insertId(entity.getInsertId())
                .updateId(entity.getUpdateId())
                .viewCount(entity.getViewCount())
                .build();
    }
}
