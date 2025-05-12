package com.example.bcsd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bcsd.model.Member;
import com.example.bcsd.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member saveMember(Member member) {
        Optional<Member> existingMember = memberRepository.findByEmail(member.getEmail());
        if (existingMember.isPresent() && !existingMember.get().getId().equals(member.getId())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }
        return memberRepository.save(member);
    }

    public void deleteMemberById(Long id) {
        memberRepository.deleteById(id);
    }
} 