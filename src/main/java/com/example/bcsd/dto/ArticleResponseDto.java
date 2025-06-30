package com.example.bcsd.dto;

import java.time.LocalDateTime;

import com.example.bcsd.model.Article;
import com.example.bcsd.model.Board;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public class ArticleResponseDto {
    
    @NotNull
    @Min(1)
    private Long id;
    
    @NotNull
    @Min(1)
    private Long memberId;
    
    @Min(1)
    private Long boardId;
    
    @Size(max = 100)
    private String boardName;
    
    @NotBlank
    @Size(min = 1, max = 200)
    private String title;
    
    @NotBlank
    @Size(min = 1, max = 5000)
    private String content;
    
    @NotNull
    @PastOrPresent
    private LocalDateTime createdAt;
    
    @NotNull
    @PastOrPresent
    private LocalDateTime updatedAt;

    public ArticleResponseDto() {
    }

    public ArticleResponseDto(Long id, Long memberId, Long boardId, String boardName, 
                            String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.boardId = boardId;
        this.boardName = boardName;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ArticleResponseDto from(Article article) {
        Board board = article.getBoard();
        return new ArticleResponseDto(
                article.getId(),
                article.getMemberId(),
                board != null ? board.getId() : null,
                board != null ? board.getName() : null,
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 