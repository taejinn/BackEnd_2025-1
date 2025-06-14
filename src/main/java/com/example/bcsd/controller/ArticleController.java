package com.example.bcsd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.bcsd.dto.ArticleRequestDto;
import com.example.bcsd.model.Article;
import com.example.bcsd.service.ArticleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    
    private final ArticleService articleService;
    
    @GetMapping("/posts")
    public String posts(Model model, HttpServletRequest request) {
        String boardId = request.getParameter("boardId");
        List<Article> articles;
        if (boardId != null && !boardId.isEmpty()) {
            articles = articleService.getArticlesByBoardId(Long.valueOf(boardId));
            model.addAttribute("boardId", boardId);
        } else {
            articles = articleService.getAllArticles();
        }
        model.addAttribute("posts", articles);
        return "posts";
    }

    @GetMapping("/articles")
    @ResponseBody
    public ResponseEntity<List<Article>> getArticles(HttpServletRequest request) {
        String boardId = request.getParameter("boardId");
        List<Article> articles;
        if (boardId != null && !boardId.isEmpty()) {
            articles = articleService.getArticlesByBoardId(Long.valueOf(boardId));
        } else {
            articles = articleService.getAllArticles();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Article> getArticle(@PathVariable("id") Long id) {
        Article article = articleService.getArticleByIdOrElseThrow(id);
        return ResponseEntity.ok(article);
    }

    @PostMapping("/article")
    @ResponseBody
    public ResponseEntity<Article> createArticle(@Valid @RequestBody ArticleRequestDto articleDto) {
        Article savedArticle = articleService.createArticle(articleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Article> updateArticle(@PathVariable("id") Long id, @Valid @RequestBody ArticleRequestDto articleDto) {
        Article updatedArticle = articleService.updateArticle(id, articleDto);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id) {
        articleService.deleteArticleById(id);
        return ResponseEntity.noContent().build();
    }
}