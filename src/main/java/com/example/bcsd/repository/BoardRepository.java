package com.example.bcsd.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.bcsd.model.Board;

@Repository
public class BoardRepository {
    private final Map<Long, Board> store = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public List<Board> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Board> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<Board> findByName(String name) {
        return store.values().stream()
                .filter(board -> board.getName().equals(name))
                .findFirst();
    }

    public Board save(Board board) {
        if (board.getId() == null) {
            board.setId(sequence.incrementAndGet());
        }
        store.put(board.getId(), board);
        return board;
    }

    public void deleteById(Long id) {
        store.remove(id);
    }
} 