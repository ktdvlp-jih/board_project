package com.rsp.platform.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity                         // JPA에서 데이터베이스 테이블과 매핑되는 클래스임을 나타냄
@Table(name = "rsp_board")      // 맵핑될 테이블 지정
@Getter                         // Lombok을 통해 getter 생성
@Setter                         // Lombok을 통해 setter 생성
@NoArgsConstructor              // 기본 생성자 자동 생성
@AllArgsConstructor             // 모든 필드를 포함한 생성자 생성
@Builder                        // 빌더 패턴 제공 (Board.builder().boardTitle("제목").build() 형태로 사용 가능)
public class BoardEntity {

    @Id     // 기본키
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rsp_board_seq") // 사용할 시퀀스 지정하면 값 자동생성
    @SequenceGenerator(name = "rsp_board_seq", sequenceName = "rsp_board_seq", allocationSize = 1)
    private Long boardId;

    @Column(nullable = false, length = 255)
    private String boardTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String boardContent;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private Boolean isDelete;

    @Column(nullable = false)
    private Boolean isEnable;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false, length = 20)
    private String insertId;

    @Column(nullable = false)
    private LocalDateTime insertDate;

    @Column(nullable = false, length = 20)
    private String updateId;

    @Column(nullable = false)
    private LocalDateTime updateDate;



    // BoardEntity.java 내부
    public static BoardEntity create(String title, String content, String insertId) {
        LocalDateTime now = LocalDateTime.now();
        return BoardEntity.builder()
                .boardTitle(title)
                .boardContent(content)
                .viewCount(0L)
                .isDelete(false)
                .isEnable(true)
                .insertId(insertId)
                .insertDate(now)
                .updateDate(now)
                .updateId(insertId)
                .build();
    }

    // 게시글 정보 수정용 도메인 메서드 (setter 없음)
    public void update(String boardTitle, String boardContent, String updateId) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.updateId = updateId;
        this.updateDate = LocalDateTime.now();
    }

    // 게시글 삭제 (소프트 딜리트)
    public void delete(String updateId) {
        this.isDelete = true;
        this.updateId = updateId;
        this.updateDate = LocalDateTime.now();
    }

    /*
    // 엔티티 저장 전 자동으로 값 초기화 (null 방지)
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.insertDate == null) this.insertDate = now;              // 등록일 없으면 현재 시각으로
        if (this.updateDate == null) this.updateDate = now;              // 수정일 없으면 현재 시각으로
        if (this.viewCount == null) this.viewCount = 0;                  // 조회수 기본값 0
        if (this.isDelete == null) this.isDelete = false;                // 삭제여부 false
        if (this.isEnable == null) this.isEnable = true;                 // 활성화 여부 true
        if (this.updateId == null) this.updateId = this.insertId;        // 수정자 없으면 작성자로 설정
    }
    */
}
