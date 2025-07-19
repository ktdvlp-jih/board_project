package com.rsp.board.service;

import com.rsp.board.entity.Board;
import com.rsp.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> getBoardList() {
        return boardRepository.findByIsDeleteFalseAndIsEnableTrueOrderByBoardIdDesc();
    }
}
