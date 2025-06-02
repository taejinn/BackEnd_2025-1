package com.example.bcsd.dao;

import com.example.bcsd.model.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MemberDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Member save(Member member) {
        if (member.getId() == null) {
            em.persist(member);
            return member;
        } else {
            return em.merge(member);
        }
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByEmail(String email) {
        try {
            Member member = em.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();
            return Optional.of(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class)
            .getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }
} 