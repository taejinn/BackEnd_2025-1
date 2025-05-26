package com.example.bcsd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bcsd.model.Board;
import com.example.bcsd.service.BoardService;

@RestController
public class BoardController {
    
    private final BoardService boardService;
    
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    
    @GetMapping("/boards")
    public ResponseEntity<List<Board>> getAllBoards() {
        List<Board> boards = boardService.findAllBoards();
        return ResponseEntity.ok(boards);
    }
    
    @GetMapping("/board/{id}")
    public ResponseEntity<Board> getBoard(@PathVariable Long id) {
        Board board = boardService.findBoardById(id);
        return ResponseEntity.ok(board);
    }
    
    @PostMapping("/board")
    public ResponseEntity<Board> createBoard(@RequestBody Board board) {
        Board savedBoard = boardService.saveBoard(board);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBoard);
    }
    
    @PutMapping("/board/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable Long id, @RequestBody Board board) {
        Board updatedBoard = boardService.updateBoard(id, board);
        return ResponseEntity.ok(updatedBoard);
    }
    
    @DeleteMapping("/board/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoardById(id);
        return ResponseEntity.noContent().build();
    }
} 