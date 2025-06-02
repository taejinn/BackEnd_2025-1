package com.example.bcsd.dto;

import jakarta.validation.constraints.NotEmpty;

public class BoardRequestDto {

    @NotEmpty(message = "게시판 이름은 필수 입력 항목입니다.")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
} 