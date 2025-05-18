package com.example.bcsd;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bcsd.model.Article;

@Service
public class PostService {
    
    private final PostRepository postRepository;
    
    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    public List<Article> getAllPosts() {
        return postRepository.findAll();
    }
    
    public Article getPostById(Integer id) {
        return postRepository.findById(id);
    }
    
    public void createPost(Article article) {
        postRepository.save(article);
    }
    
    public Article updatePost(Integer id, Article requestArticle) {
        Article article = postRepository.findById(id);
        if (article != null) {
            article.setTitle(requestArticle.getTitle());
            article.setContent(requestArticle.getContent());
            article.setMemberId(requestArticle.getMemberId());
        }
        return article;
    }
    
    public boolean deletePost(Integer id) {
        Article article = postRepository.findById(id);
        if (article == null) {
            return false;
        }
        postRepository.deleteById(id);
        return true;
    }
} 