package com.example.bcsd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.dao.MemberDao;
import com.example.bcsd.model.Member;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> findAllMembers() {
        return memberDao.findAll();
    }

    public Optional<Member> findMemberById(Long id) {
        return memberDao.findById(id);
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberDao.findByEmail(email);
    }

    @Transactional
    public Member saveMember(Member member) {
        Optional<Member> existingMember = memberDao.findByEmail(member.getEmail());
        if (existingMember.isPresent() && !existingMember.get().getId().equals(member.getId())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }
        return memberDao.save(member);
    }

    @Transactional
    public void deleteMemberById(Long id) {
        memberDao.deleteById(id);
    }
} 