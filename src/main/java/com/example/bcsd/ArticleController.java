package com.example.bcsd;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

    @GetMapping("/article")
    public ResponseEntity<List<Post>> getArticles() {
        List<Post> posts = Post.findAll();
        if (posts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<Post> getArticle(@PathVariable("id") Integer id) {
        Post post = Post.findById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping("/article")
    public ResponseEntity<Post> createArticle(@RequestBody Post post) {
        post.save();
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/article/{id}")
    public ResponseEntity<Post> updateArticle(@PathVariable("id") Integer id, @RequestBody Post requestPost) {
        Post post = Post.findById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String title = requestPost.getTitle();
        String author = requestPost.getAuthor();
        String content = requestPost.getContent();
        post.update(title, author, content);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Integer id) {
        Post post = Post.findById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Post.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
