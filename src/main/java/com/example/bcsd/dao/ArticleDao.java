package com.example.bcsd.dao;

import com.example.bcsd.model.Article;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ArticleDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Article save(Article article) {
        if (article.getId() == null) {
            em.persist(article);
            return article;
        } else {
            return em.merge(article);
        }
    }

    public Optional<Article> findById(Long id) {
        Article article = em.find(Article.class, id);
        return Optional.ofNullable(article);
    }

    public List<Article> findByMemberId(Long memberId) {
        return em.createQuery("SELECT a FROM Article a WHERE a.memberId = :memberId", Article.class)
            .setParameter("memberId", memberId)
            .getResultList();
    }

    public List<Article> findByBoardId(Long boardId) {
        return em.createQuery("SELECT a FROM Article a WHERE a.boardId = :boardId", Article.class)
            .setParameter("boardId", boardId)
            .getResultList();
    }

    public List<Article> findAll() {
        return em.createQuery("SELECT a FROM Article a", Article.class)
            .getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }
} 