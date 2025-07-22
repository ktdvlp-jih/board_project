package com.rsp.platform.domain.board.dto;

import com.rsp.platform.domain.board.entity.BoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardListResponse {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String insertId;
    private String updateId;
    private Long viewCount;
    private MultipartFile[] files; // 파일 배열도 추가!


    public static BoardListResponse fromEntity(BoardEntity entity) {
        return BoardListResponse.builder()
                .boardId(entity.getBoardId())
                .boardTitle(entity.getBoardTitle())
                .boardContent(entity.getBoardContent())
                .insertId(entity.getInsertId())
                .updateId(entity.getUpdateId())
                .viewCount(entity.getViewCount())
                .build();
    }

}
