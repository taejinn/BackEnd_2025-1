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

import com.example.bcsd.model.Board;

@Repository
public class BoardDao {
    private final JdbcTemplate jdbcTemplate;

    public BoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Board> boardRowMapper = (rs, rowNum) -> {
        Board board = new Board();
        board.setId(rs.getLong("id"));
        board.setName(rs.getString("name"));
        return board;
    };

    public List<Board> findAll() {
        String sql = "SELECT * FROM board";
        return jdbcTemplate.query(sql, boardRowMapper);
    }

    public Optional<Board> findById(Long id) {
        String sql = "SELECT * FROM board WHERE id = ?";
        List<Board> results = jdbcTemplate.query(sql, boardRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<Board> findByName(String name) {
        String sql = "SELECT * FROM board WHERE name = ?";
        List<Board> results = jdbcTemplate.query(sql, boardRowMapper, name);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Board save(Board board) {
        if (board.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = "INSERT INTO board (name) VALUES (?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, board.getName());
                return ps;
            }, keyHolder);
            
            Long newId = Objects.requireNonNull(keyHolder.getKey()).longValue();
            board.setId(newId);
            return findById(newId).orElse(board);
        } else {
            String sql = "UPDATE board SET name = ? WHERE id = ?";
            jdbcTemplate.update(sql, board.getName(), board.getId());
            return board;
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM board WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
} 