package com.example.bcsd;

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

@Controller
public class ArticleController {
    
    private final PostService postService;
    
    @Autowired
    public ArticleController(PostService postService) {
        this.postService = postService;
    }
    
    @GetMapping("/posts")
    public String posts(Model model) {
        List<Article> articles = postService.getAllPosts();
        model.addAttribute("posts", articles);
        return "posts";
    }

    @GetMapping("/articles")
    @ResponseBody
    public ResponseEntity<List<Article>> getArticles() {
        List<Article> articles = postService.getAllPosts();
        if (articles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Article> getArticle(@PathVariable("id") Integer id) {
        Article article = postService.getPostById(id);
        if (article == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(article);
    }

    @PostMapping("/article")
    @ResponseBody
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        postService.createPost(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    @PutMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Article> updateArticle(@PathVariable("id") Integer id, @RequestBody Article requestArticle) {
        Article article = postService.updatePost(id, requestArticle);
        if (article == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(article);
    }

    @DeleteMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Integer id) {
        boolean deleted = postService.deletePost(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }
}