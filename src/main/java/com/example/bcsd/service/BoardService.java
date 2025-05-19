package com.example.bcsd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.dao.BoardDao;
import com.example.bcsd.model.Board;

@Service
public class BoardService {
    private final BoardDao boardDao;

    public BoardService(BoardDao boardDao) {
        this.boardDao = boardDao;
    }

    public List<Board> findAllBoards() {
        return boardDao.findAll();
    }

    public Optional<Board> findBoardById(Long id) {
        return boardDao.findById(id);
    }

    public Optional<Board> findBoardByName(String name) {
        return boardDao.findByName(name);
    }

    @Transactional
    public Board saveBoard(Board board) {
        Optional<Board> existingBoard = boardDao.findByName(board.getName());
        if (existingBoard.isPresent() && !existingBoard.get().getId().equals(board.getId())) {
            throw new IllegalStateException("이미 사용 중인 게시판 이름입니다.");
        }
        return boardDao.save(board);
    }

    @Transactional
    public void deleteBoardById(Long id) {
        boardDao.deleteById(id);
    }
} 