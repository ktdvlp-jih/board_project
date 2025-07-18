package com.rsupport.board.service;

import com.rsupport.board.entity.Board;
import com.rsupport.board.repository.BoardRepository;
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
