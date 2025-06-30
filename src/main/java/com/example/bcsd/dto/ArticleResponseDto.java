package com.example.bcsd.dto;

import java.time.LocalDateTime;

import com.example.bcsd.model.Article;
import com.example.bcsd.model.Board;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
} 