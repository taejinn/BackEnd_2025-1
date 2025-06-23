package com.example.bcsd.dto;

import jakarta.validation.constraints.Positive;

public class ArticleSearchRequestDto {

    @Positive(message = "게시판 ID는 양의 정수여야 합니다.")
    private Long boardId;

    public ArticleSearchRequestDto() {
    }

    public ArticleSearchRequestDto(Long boardId) {
        this.boardId = boardId;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }
} 