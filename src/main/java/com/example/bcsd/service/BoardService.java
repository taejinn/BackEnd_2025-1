package com.example.bcsd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.dao.ArticleDao;
import com.example.bcsd.dao.BoardDao;
import com.example.bcsd.dto.BoardRequestDto;
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

    public List<Board> getAllBoards() {
        return boardDao.findAll();
    }

    public Board getBoardById(Long id) {
        return boardDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시판입니다."));
    }

    public Optional<Board> getBoardByName(String name) {
        return boardDao.findByName(name);
    }

    @Transactional
    public Board createBoard(BoardRequestDto boardDto) {
        Board board = new Board();
        board.setName(boardDto.getName());

        Optional<Board> existingBoard = boardDao.findByName(board.getName());
        if (existingBoard.isPresent() && (board.getId() == null || !existingBoard.get().getId().equals(board.getId()))) {
            throw new DuplicateResourceException("이미 사용 중인 게시판 이름입니다.");
        }
        return boardDao.save(board);
    }

    @Transactional
    public Board updateBoard(Long id, BoardRequestDto boardDto) {
        Board existingBoardEntity = getBoardById(id);
        
        if (!existingBoardEntity.getName().equals(boardDto.getName())) {
            Optional<Board> duplicateNameBoard = boardDao.findByName(boardDto.getName());
            if (duplicateNameBoard.isPresent()) {
                throw new DuplicateResourceException("이미 사용 중인 게시판 이름입니다.");
            }
        }
        
        existingBoardEntity.setName(boardDto.getName());
        
        return boardDao.save(existingBoardEntity);
    }

    @Transactional
    public void deleteBoardById(Long id) {
        Board board = getBoardById(id);
        
        List<Article> boardArticles = articleDao.findByBoardId(id);
        if (!boardArticles.isEmpty()) {
            throw new InvalidRequestException("해당 게시판에 작성된 게시물이 존재하여 삭제할 수 없습니다.");
        }
        
        boardDao.deleteById(id);
    }
} 