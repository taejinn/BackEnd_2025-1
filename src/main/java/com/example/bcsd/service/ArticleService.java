package com.example.bcsd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bcsd.model.Article;
import com.example.bcsd.repository.ArticleRepository;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> findArticleById(Long id) {
        return articleRepository.findById(id);
    }
    
    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public List<Article> findArticlesByMemberId(String memberId) {
        return articleRepository.findByMemberId(memberId);
    }

    public List<Article> findArticlesByBoardId(String boardId) {
        return articleRepository.findByBoardId(boardId);
    }

    public Article saveArticle(Article article) {
        LocalDateTime now = LocalDateTime.now();
        
        if (article.getId() == null) {
            article.setCreatedAt(now);
            article.setUpdatedAt(now);
        } else {
            Optional<Article> existingArticle = articleRepository.findById(article.getId());
            if (existingArticle.isPresent()) {
                article.setCreatedAt(existingArticle.get().getCreatedAt());
                article.setUpdatedAt(now);
            } else {
                article.setCreatedAt(now);
                article.setUpdatedAt(now);
            }
        }
        
        return articleRepository.save(article);
    }
    
    public Article updateArticle(Long id, Article updatedArticle) {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isEmpty()) {
            return null;
        }
        
        Article existingArticle = articleOptional.get();
        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setContent(updatedArticle.getContent());
        existingArticle.setMemberId(updatedArticle.getMemberId());
        existingArticle.setBoardId(updatedArticle.getBoardId());
        existingArticle.setUpdatedAt(LocalDateTime.now());
        
        return articleRepository.save(existingArticle);
    }

    public void deleteArticleById(Long id) {
        articleRepository.deleteById(id);
    }
    
    public boolean deleteArticleIfExists(Long id) {
        if (articleRepository.findById(id).isEmpty()) {
            return false;
        }
        
        articleRepository.deleteById(id);
        return true;
    }
} 