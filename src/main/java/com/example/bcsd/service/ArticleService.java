package com.example.bcsd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.dao.ArticleDao;
import com.example.bcsd.dao.BoardDao;
import com.example.bcsd.dao.MemberDao;
import com.example.bcsd.exception.InvalidRequestException;
import com.example.bcsd.exception.ResourceNotFoundException;
import com.example.bcsd.model.Article;

@Service
public class ArticleService {
    private final ArticleDao articleDao;
    private final MemberDao memberDao;
    private final BoardDao boardDao;

    public ArticleService(ArticleDao articleDao, MemberDao memberDao, BoardDao boardDao) {
        this.articleDao = articleDao;
        this.memberDao = memberDao;
        this.boardDao = boardDao;
    }

    public List<Article> findAllArticles() {
        return articleDao.findAll();
    }

    public Article findArticleById(Long id) {
        return articleDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시물입니다."));
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
        validateArticleFields(article);
        validateMemberExists(article.getMemberId());
        validateBoardExists(article.getBoardId());
        
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
        Article existingArticle = findArticleById(id);
        
        validateArticleFields(updatedArticle);
        validateMemberExists(updatedArticle.getMemberId());
        validateBoardExists(updatedArticle.getBoardId());
        
        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setContent(updatedArticle.getContent());
        existingArticle.setMemberId(updatedArticle.getMemberId());
        existingArticle.setBoardId(updatedArticle.getBoardId());
        existingArticle.setUpdatedAt(LocalDateTime.now());
        
        return articleDao.save(existingArticle);
    }

    @Transactional
    public void deleteArticleById(Long id) {
        Article article = findArticleById(id);
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

    private void validateArticleFields(Article article) {
        if (article.getTitle() == null || article.getContent() == null ||
            article.getMemberId() == null || article.getBoardId() == null) {
            throw new InvalidRequestException("게시물 정보의 필수 필드가 누락되었습니다.");
        }
    }

    private void validateMemberExists(Long memberId) {
        if (memberDao.findById(memberId).isEmpty()) {
            throw new InvalidRequestException("존재하지 않는 사용자를 참조할 수 없습니다.");
        }
    }

    private void validateBoardExists(Long boardId) {
        if (boardDao.findById(boardId).isEmpty()) {
            throw new InvalidRequestException("존재하지 않는 게시판을 참조할 수 없습니다.");
        }
    }
} 