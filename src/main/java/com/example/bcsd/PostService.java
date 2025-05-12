package com.example.bcsd;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    
    private final PostRepository postRepository;
    
    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    
    public Post getPostById(Integer id) {
        return postRepository.findById(id);
    }
    
    public void createPost(Post post) {
        postRepository.save(post);
    }
    
    public Post updatePost(Integer id, Post requestPost) {
        Post post = postRepository.findById(id);
        if (post != null) {
            post.update(requestPost.getTitle(), requestPost.getAuthor(), requestPost.getContent());
        }
        return post;
    }
    
    public boolean deletePost(Integer id) {
        Post post = postRepository.findById(id);
        if (post == null) {
            return false;
        }
        postRepository.deleteById(id);
        return true;
    }
} 