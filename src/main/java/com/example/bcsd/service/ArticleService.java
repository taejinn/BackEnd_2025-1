package com.example.bcsd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.dao.ArticleDao;
import com.example.bcsd.model.Article;

@Service
public class ArticleService {
    private final ArticleDao articleDao;

    public ArticleService(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public List<Article> findAllArticles() {
        return articleDao.findAll();
    }

    public Optional<Article> findArticleById(Long id) {
        return articleDao.findById(id);
    }
    
    public Article getArticleById(Long id) {
        return articleDao.findById(id).orElse(null);
    }

    public List<Article> findArticlesByMemberId(Long memberId) {
        return articleDao.findByMemberId(memberId);
    }

    public List<Article> findArticlesByBoardId(Long boardId) {
        return articleDao.findByBoardId(boardId);
    }

    @Transactional
    public Article saveArticle(Article article) {
        LocalDateTime now = LocalDateTime.now();
        
        if (article.getId() == null) {
            article.setCreatedAt(now);
            article.setUpdatedAt(now);
        } else {
            Optional<Article> existingArticle = articleDao.findById(article.getId());
            if (existingArticle.isPresent()) {
                article.setCreatedAt(existingArticle.get().getCreatedAt());
                article.setUpdatedAt(now);
            } else {
                article.setCreatedAt(now);
                article.setUpdatedAt(now);
            }
        }
        
        return articleDao.save(article);
    }
    
    @Transactional
    public Article updateArticle(Long id, Article updatedArticle) {
        Optional<Article> articleOptional = articleDao.findById(id);
        if (articleOptional.isEmpty()) {
            return null;
        }
        
        Article existingArticle = articleOptional.get();
        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setContent(updatedArticle.getContent());
        existingArticle.setMemberId(updatedArticle.getMemberId());
        existingArticle.setBoardId(updatedArticle.getBoardId());
        existingArticle.setUpdatedAt(LocalDateTime.now());
        
        return articleDao.save(existingArticle);
    }

    @Transactional
    public void deleteArticleById(Long id) {
        articleDao.deleteById(id);
    }
    
    @Transactional
    public boolean deleteArticleIfExists(Long id) {
        if (articleDao.findById(id).isEmpty()) {
            return false;
        }
        
        articleDao.deleteById(id);
        return true;
    }
} 