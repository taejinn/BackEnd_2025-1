package com.example.bcsd;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.bcsd.model.Article;

@Repository
public class PostRepository {
    
    private static List<Article> allArticles = new ArrayList<>();
    
    public List<Article> findAll() {
        return allArticles;
    }
    
    public Article findById(Integer id) {
        if (id >= 0 && id < allArticles.size()) {
            return allArticles.get(id);
        }
        return null;
    }
    
    public void save(Article article) {
        if (article.getId() == null) {
            article.setId((long) allArticles.size());
        }
        if (article.getCreatedAt() == null) {
            article.setCreatedAt(LocalDateTime.now());
        }
        article.setUpdatedAt(LocalDateTime.now());
        allArticles.add(article);
    }
    
    public void deleteById(Integer id) {
        if (id >= 0 && id < allArticles.size()) {
            allArticles.remove(id.intValue());
            
            for (int i = id; i < allArticles.size(); i++) {
                allArticles.get(i).setId((long) i);
            }
        }
    }
}