package com.example.bcsd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bcsd.model.Board;
import com.example.bcsd.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<Board> findAllBoards() {
        return boardRepository.findAll();
    }

    public Optional<Board> findBoardById(Long id) {
        return boardRepository.findById(id);
    }

    public Optional<Board> findBoardByName(String name) {
        return boardRepository.findByName(name);
    }

    public Board saveBoard(Board board) {
        Optional<Board> existingBoard = boardRepository.findByName(board.getName());
        if (existingBoard.isPresent() && !existingBoard.get().getId().equals(board.getId())) {
            throw new IllegalStateException("이미 사용 중인 게시판 이름입니다.");
        }
        return boardRepository.save(board);
    }

    public void deleteBoardById(Long id) {
        boardRepository.deleteById(id);
    }
} 