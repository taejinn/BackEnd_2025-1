package com.example.bcsd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.bcsd.dto.ArticleRequestDto;
import com.example.bcsd.dto.ArticleResponseDto;
import com.example.bcsd.dto.ArticleSearchRequestDto;
import com.example.bcsd.service.ArticleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    
    private final ArticleService articleService;
    
    @GetMapping("/posts")
    public String posts(Model model, @Valid @ModelAttribute ArticleSearchRequestDto searchDto) {
        List<ArticleResponseDto> articles;
        if (searchDto.getBoardId() != null) {
            articles = articleService.getArticlesByBoardId(searchDto.getBoardId());
            model.addAttribute("boardId", searchDto.getBoardId());
        } else {
            articles = articleService.getAllArticles();
        }
        model.addAttribute("posts", articles);
        return "posts";
    }

    @GetMapping("/articles")
    @ResponseBody
    public ResponseEntity<List<ArticleResponseDto>> getArticles(@Valid @ModelAttribute ArticleSearchRequestDto searchDto) {
        List<ArticleResponseDto> articles;
        if (searchDto.getBoardId() != null) {
            articles = articleService.getArticlesByBoardId(searchDto.getBoardId());
        } else {
            articles = articleService.getAllArticles();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<ArticleResponseDto> getArticle(@PathVariable("id") Long id) {
        ArticleResponseDto article = articleService.getArticleByIdOrElseThrow(id);
        return ResponseEntity.ok(article);
    }

    @PostMapping("/article")
    @ResponseBody
    public ResponseEntity<ArticleResponseDto> createArticle(@Valid @RequestBody ArticleRequestDto articleDto) {
        ArticleResponseDto savedArticle = articleService.createArticle(articleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<ArticleResponseDto> updateArticle(@PathVariable("id") Long id, @Valid @RequestBody ArticleRequestDto articleDto) {
        ArticleResponseDto updatedArticle = articleService.updateArticle(id, articleDto);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id) {
        articleService.deleteArticleById(id);
        return ResponseEntity.noContent().build();
    }
}