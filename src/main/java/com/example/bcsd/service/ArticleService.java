package com.example.bcsd.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.dao.ArticleDao;
import com.example.bcsd.dao.BoardDao;
import com.example.bcsd.dao.MemberDao;
import com.example.bcsd.dto.ArticleRequestDto;
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

    public List<Article> getAllArticles() {
        return articleDao.findAll();
    }

    public Article getArticleByIdOrElseThrow(Long id) {
        return articleDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시물입니다."));
    }
    
    public Article getArticleById(Long id) {
        return articleDao.findById(id).orElse(null);
    }

    public List<Article> getArticlesByMemberId(Long memberId) {
        return articleDao.findByMemberId(memberId);
    }

    public List<Article> getArticlesByBoardId(Long boardId) {
        return articleDao.findByBoardId(boardId);
    }

    @Transactional
    public Article createArticle(ArticleRequestDto articleDto) {
        validateMemberExists(articleDto.getMemberId());
        validateBoardExists(articleDto.getBoardId());
        
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setMemberId(articleDto.getMemberId());
        article.setBoardId(articleDto.getBoardId());
        
        return articleDao.save(article);
    }
    
    @Transactional
    public Article updateArticle(Long id, ArticleRequestDto articleDto) {
        Article existingArticle = getArticleByIdOrElseThrow(id);
        
        validateMemberExists(articleDto.getMemberId());
        validateBoardExists(articleDto.getBoardId());
        
        existingArticle.setTitle(articleDto.getTitle());
        existingArticle.setContent(articleDto.getContent());
        existingArticle.setMemberId(articleDto.getMemberId());
        existingArticle.setBoardId(articleDto.getBoardId());
        
        return articleDao.save(existingArticle);
    }

    @Transactional
    public void deleteArticleById(Long id) {
        Article article = getArticleByIdOrElseThrow(id);
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