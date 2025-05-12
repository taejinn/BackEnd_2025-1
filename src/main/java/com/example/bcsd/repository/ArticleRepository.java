package com.example.bcsd.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.bcsd.model.Article;

@Repository
public class ArticleRepository {
    private final Map<Long, Article> store = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public List<Article> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Article> findByMemberId(String memberId) {
        return store.values().stream()
                .filter(article -> article.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    public List<Article> findByBoardId(String boardId) {
        return store.values().stream()
                .filter(article -> article.getBoardId().equals(boardId))
                .collect(Collectors.toList());
    }

    public Article save(Article article) {
        if (article.getId() == null) {
            article.setId(sequence.incrementAndGet());
        }
        store.put(article.getId(), article);
        return article;
    }

    public void deleteById(Long id) {
        store.remove(id);
    }
} 