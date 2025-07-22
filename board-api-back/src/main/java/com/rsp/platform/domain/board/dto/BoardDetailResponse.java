package com.rsp.platform.domain.board.dto;

import com.rsp.platform.domain.board.entity.BoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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
    private Boolean isDelete;
    private Boolean isEnable;
    private String insertId;
    private LocalDateTime insertDate;
    private String updateId;
    private LocalDateTime updateDate;
    private MultipartFile[] files; // 파일 배열도 추가!


    public static BoardDetailResponse fromEntity(BoardEntity entity) {
        return BoardDetailResponse.builder()
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
