package com.example.bcsd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.dao.ArticleDao;
import com.example.bcsd.dao.BoardDao;
import com.example.bcsd.exception.DuplicateResourceException;
import com.example.bcsd.exception.InvalidRequestException;
import com.example.bcsd.exception.ResourceNotFoundException;
import com.example.bcsd.model.Article;
import com.example.bcsd.model.Board;

@Service
public class BoardService {
    private final BoardDao boardDao;
    private final ArticleDao articleDao;

    public BoardService(BoardDao boardDao, ArticleDao articleDao) {
        this.boardDao = boardDao;
        this.articleDao = articleDao;
    }

    public List<Board> findAllBoards() {
        return boardDao.findAll();
    }

    public Board findBoardById(Long id) {
        return boardDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시판입니다."));
    }

    public Optional<Board> findBoardByName(String name) {
        return boardDao.findByName(name);
    }

    @Transactional
    public Board saveBoard(Board board) {
        validateBoardFields(board);
        
        Optional<Board> existingBoard = boardDao.findByName(board.getName());
        if (existingBoard.isPresent() && !existingBoard.get().getId().equals(board.getId())) {
            throw new DuplicateResourceException("이미 사용 중인 게시판 이름입니다.");
        }
        return boardDao.save(board);
    }

    @Transactional
    public Board updateBoard(Long id, Board updatedBoard) {
        Board existingBoard = findBoardById(id);
        
        if (!existingBoard.getName().equals(updatedBoard.getName())) {
            Optional<Board> duplicateNameBoard = boardDao.findByName(updatedBoard.getName());
            if (duplicateNameBoard.isPresent()) {
                throw new DuplicateResourceException("이미 사용 중인 게시판 이름입니다.");
            }
        }
        
        validateBoardFields(updatedBoard);
        
        existingBoard.setName(updatedBoard.getName());
        
        return boardDao.save(existingBoard);
    }

    @Transactional
    public void deleteBoardById(Long id) {
        Board board = findBoardById(id);
        
        List<Article> boardArticles = articleDao.findByBoardId(id);
        if (!boardArticles.isEmpty()) {
            throw new InvalidRequestException("해당 게시판에 작성된 게시물이 존재하여 삭제할 수 없습니다.");
        }
        
        boardDao.deleteById(id);
    }

    private void validateBoardFields(Board board) {
        if (board.getName() == null) {
            throw new InvalidRequestException("게시판 정보의 필수 필드가 누락되었습니다.");
        }
    }
} 