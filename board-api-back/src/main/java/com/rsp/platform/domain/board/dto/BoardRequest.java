package com.rsp.platform.domain.board.dto;

import com.rsp.platform.domain.board.entity.BoardEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequest {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String insertId;
    private String updateId;
    private Long viewCount;

    public static BoardRequest fromEntity(BoardEntity entity) {
        return BoardRequest.builder()
                .boardId(entity.getBoardId())
                .boardTitle(entity.getBoardTitle())
                .boardContent(entity.getBoardContent())
                .insertId(entity.getInsertId())
                .updateId(entity.getUpdateId())
                .viewCount(entity.getViewCount())
                .build();
    }
}
