package com.example.bcsd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.dto.BoardRequestDto;
import com.example.bcsd.exception.DuplicateResourceException;
import com.example.bcsd.exception.ResourceNotFoundException;
import com.example.bcsd.model.Board;
import com.example.bcsd.repository.BoardRepository;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시판입니다."));
    }

    public Optional<Board> getBoardByName(String name) {
        return boardRepository.findByName(name);
    }

    @Transactional
    public Board createBoard(BoardRequestDto boardDto) {
        Board board = new Board();
        board.setName(boardDto.getName());

        Optional<Board> existingBoard = boardRepository.findByName(board.getName());
        
        if (existingBoard.isPresent()) {
            boolean isNewBoard = (board.getId() == null);
            boolean isDifferentBoard = !existingBoard.get().getId().equals(board.getId());
            
            if (isNewBoard || isDifferentBoard) {
                throw new DuplicateResourceException("이미 사용 중인 게시판 이름입니다.");
            }
        }
        
        return boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(Long id, BoardRequestDto boardDto) {
        Board existingBoardEntity = getBoardById(id);
        
        if (existingBoardEntity.getName().equals(boardDto.getName())) {
            return boardRepository.save(existingBoardEntity);
        }
        
        Optional<Board> duplicateNameBoard = boardRepository.findByName(boardDto.getName());
        if (duplicateNameBoard.isPresent()) {
            throw new DuplicateResourceException("이미 사용 중인 게시판 이름입니다.");
        }
        
        existingBoardEntity.setName(boardDto.getName());
        return boardRepository.save(existingBoardEntity);
    }

    @Transactional
    public void deleteBoardById(Long id) {
        Board board = getBoardById(id);
        boardRepository.deleteById(id);
    }
} 