package com.example.bcsd.dao;

import com.example.bcsd.model.Board;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BoardDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Board save(Board board) {
        if (board.getId() == null) {
            em.persist(board);
            return board;
        } else {
            return em.merge(board);
        }
    }

    public Optional<Board> findById(Long id) {
        Board board = em.find(Board.class, id);
        return Optional.ofNullable(board);
    }

    public Optional<Board> findByName(String name) {
        List<Board> boards = em.createQuery("SELECT b FROM Board b WHERE b.name = :name", Board.class)
            .setParameter("name", name)
            .getResultList();
        return boards.stream().findFirst();
    }

    public List<Board> findAll() {
        return em.createQuery("SELECT b FROM Board b", Board.class)
            .getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }
} 