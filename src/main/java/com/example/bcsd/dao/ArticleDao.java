package com.example.bcsd.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.bcsd.model.Article;

@Repository
public class ArticleDao {
    private final JdbcTemplate jdbcTemplate;

    public ArticleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Article> articleRowMapper = (rs, rowNum) -> {
        Article article = new Article();
        article.setId(rs.getLong("id"));
        article.setMemberId(rs.getLong("author_id"));
        article.setBoardId(rs.getLong("board_id"));
        article.setTitle(rs.getString("title"));
        article.setContent(rs.getString("content"));
        article.setCreatedAt(rs.getTimestamp("created_date").toLocalDateTime());
        article.setUpdatedAt(rs.getTimestamp("modified_date").toLocalDateTime());
        return article;
    };

    public List<Article> findAll() {
        String sql = "SELECT * FROM article";
        return jdbcTemplate.query(sql, articleRowMapper);
    }

    public Optional<Article> findById(Long id) {
        String sql = "SELECT * FROM article WHERE id = ?";
        List<Article> results = jdbcTemplate.query(sql, articleRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Article> findByMemberId(Long memberId) {
        String sql = "SELECT * FROM article WHERE author_id = ?";
        return jdbcTemplate.query(sql, articleRowMapper, memberId);
    }

    public List<Article> findByBoardId(Long boardId) {
        String sql = "SELECT * FROM article WHERE board_id = ?";
        return jdbcTemplate.query(sql, articleRowMapper, boardId);
    }

    public Article save(Article article) {
        if (article.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = "INSERT INTO article (author_id, board_id, title, content) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, article.getMemberId());
                ps.setLong(2, article.getBoardId());
                ps.setString(3, article.getTitle());
                ps.setString(4, article.getContent());
                return ps;
            }, keyHolder);
            
            Long newId = Objects.requireNonNull(keyHolder.getKey()).longValue();
            article.setId(newId);
            return findById(newId).orElse(article);
        } else {
            String sql = "UPDATE article SET author_id = ?, board_id = ?, title = ?, content = ? WHERE id = ?";
            jdbcTemplate.update(sql, article.getMemberId(), article.getBoardId(), article.getTitle(), article.getContent(), article.getId());
            return article;
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM article WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
} 