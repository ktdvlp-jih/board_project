package com.rsp.platform.domain.file.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity                             // JPA에서 데이터베이스 테이블과 매핑되는 클래스임을 나타냄
@Table(name = "rsp_link_file")      // 맵핑될 테이블 지정
@Getter                             // Lombok을 통해 getter 생성
@Setter                             // Lombok을 통해 setter 생성
@NoArgsConstructor                  // 기본 생성자 자동 생성
@AllArgsConstructor                 // 모든 필드를 포함한 생성자 생성
@Builder                            // 빌더 패턴 제공 (Board.builder().boardTitle("제목").build() 형태로 사용 가능)
public class LinkFileEntity {

    @Id     // 기본키
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rsp_link_file_seq") // 사용할 시퀀스 지정하면 값 자동생성
    @SequenceGenerator(name = "rsp_link_file_seq", sequenceName = "rsp_link_file_seq", allocationSize = 1)
    private Long linkFileId;

    private Long refId;              // 게시글 PK 등 연결 대상
    private Long attachFileId;       // 파일 PK
    private Integer fileOrder;       // 파일 순서 (옵션)
    private String fileTag;          // 파일 유형 등(옵션)
    private String insertId;
    private LocalDateTime insertDate;

    @PrePersist
    public void prePersist() {
        if (this.insertDate == null) this.insertDate = LocalDateTime.now();
    }
}
