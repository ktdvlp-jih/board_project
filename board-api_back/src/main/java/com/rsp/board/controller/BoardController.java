package com.rsp.board.controller;

import com.rsp.board.entity.Board;
import com.rsp.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public List<Board> getBoardList() {
        System.out.println("getBoards 확인");
        log.info("요청 들어옴");
        return boardService.getBoardList();
    }
}
