package com.example.bcsd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.bcsd.model.Article;
import com.example.bcsd.service.ArticleService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ArticleController {
    
    private final ArticleService articleService;
    
    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }
    
    @GetMapping("/posts")
    public String posts(Model model, HttpServletRequest request) {
        String boardId = request.getParameter("boardId");
        List<Article> articles;
        if (boardId != null && !boardId.isEmpty()) {
            articles = articleService.findArticlesByBoardId(Long.valueOf(boardId));
            model.addAttribute("boardId", boardId);
        } else {
            articles = articleService.findAllArticles();
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
            articles = articleService.findArticlesByBoardId(Long.valueOf(boardId));
        } else {
            articles = articleService.findAllArticles();
        }

        if (articles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(articles);
    }

    @GetMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Article> getArticle(@PathVariable("id") Long id) {
        Article article = articleService.getArticleById(id);
        if (article == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(article);
    }

    @PostMapping("/article")
    @ResponseBody
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article savedArticle = articleService.saveArticle(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Article> updateArticle(@PathVariable("id") Long id, @RequestBody Article requestArticle) {
        Article updatedArticle = articleService.updateArticle(id, requestArticle);
        if (updatedArticle == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id) {
        boolean deleted = articleService.deleteArticleIfExists(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }
}