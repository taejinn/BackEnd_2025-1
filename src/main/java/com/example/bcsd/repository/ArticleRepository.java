package com.example.bcsd.repository;

import com.example.bcsd.model.Article;
import com.example.bcsd.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByMemberId(Long memberId);
    List<Article> findByBoard(Board board);
    List<Article> findByBoardId(Long boardId);
} 