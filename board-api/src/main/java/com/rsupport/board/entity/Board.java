package com.rsupport.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rsupport_board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq_gen")
    @SequenceGenerator(name = "board_seq_gen", sequenceName = "rsupport_board_seq", allocationSize = 1)
    private Long boardId;

    @Column(nullable = false, length = 255)
    private String boardTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String boardContent;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false)
    private Boolean isDelete;

    @Column(nullable = false)
    private Boolean isEnable;

    @Column(nullable = false, length = 20)
    private String insertId;

    @Column(nullable = false)
    private LocalDateTime insertDate;

    @Column(nullable = false, length = 20)
    private String updateId;

    @Column(nullable = false)
    private LocalDateTime updateDate;
}
