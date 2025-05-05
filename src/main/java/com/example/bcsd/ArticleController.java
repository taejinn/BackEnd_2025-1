package com.example.bcsd;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ArticleController {

    @GetMapping("/article/{id}")
    public ResponseEntity<Post> getArticle(@PathVariable("id") Integer id) {
        Post post = new Post();
        String description = post.getDescription(id);
        if (description == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        post.setDescription(description);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/article")
    public ResponseEntity<Post> createArticle(@RequestBody Post post) {
        post.setPosts();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/article/{id}")
    public ResponseEntity<Post> updateArticle(@PathVariable("id") Integer id, @RequestBody Post post) {
        String existingDescription = post.getDescription(id);
        if (existingDescription == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        post.setId(id);
        List<Map<String, Object>> posts = post.getPosts();
        if (id >= 0 && id < posts.size()) {
            posts.get(id).put("description", post.getDescription());
        }

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Integer id) {
        Post post = new Post();
        String existingDescription = post.getDescription(id);
        if (existingDescription == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        post.deletePosts(id);
        return ResponseEntity.noContent().build();
    }
}
