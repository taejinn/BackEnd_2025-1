package com.example.bcsd;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class PostRepository {
    
    public List<Post> findAll() {
        return Post.findAll();
    }
    
    public Post findById(Integer id) {
        return Post.findById(id);
    }
    
    public void save(Post post) {
        post.save();
    }
    
    public void deleteById(Integer id) {
        Post.deleteById(id);
    }
} 