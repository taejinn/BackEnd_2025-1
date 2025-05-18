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

@Controller
public class ArticleController {
    
    private final PostService postService;
    
    @Autowired
    public ArticleController(PostService postService) {
        this.postService = postService;
    }
    
    @GetMapping("/posts")
    public String posts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/articles")
    @ResponseBody
    public ResponseEntity<List<Post>> getArticles() {
        List<Post> posts = postService.getAllPosts();
        if (posts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Post> getArticle(@PathVariable("id") Integer id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping("/article")
    @ResponseBody
    public ResponseEntity<Post> createArticle(@RequestBody Post post) {
        postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<Post> updateArticle(@PathVariable("id") Integer id, @RequestBody Post requestPost) {
        Post post = postService.updatePost(id, requestPost);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(post);
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