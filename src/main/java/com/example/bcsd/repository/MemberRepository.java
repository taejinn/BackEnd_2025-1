package com.example.bcsd.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.bcsd.model.Member;

@Repository
public class MemberRepository {
    private final Map<Long, Member> store = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<Member> findByEmail(String email) {
        return store.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst();
    }

    public Member save(Member member) {
        if (member.getId() == null) {
            member.setId(sequence.incrementAndGet());
        }
        store.put(member.getId(), member);
        return member;
    }

    public void deleteById(Long id) {
        store.remove(id);
    }
} 