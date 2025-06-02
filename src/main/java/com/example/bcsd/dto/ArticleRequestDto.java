package com.example.bcsd.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ArticleRequestDto {

    @NotEmpty(message = "제목은 필수 입력 항목입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수 입력 항목입니다.")
    private String content;

    @NotNull(message = "작성자 ID는 필수 입력 항목입니다.")
    private Long memberId;

    @NotNull(message = "게시판 ID는 필수 입력 항목입니다.")
    private Long boardId;

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
}
