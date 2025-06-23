package com.example.bcsd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.repository.ArticleRepository;
import com.example.bcsd.repository.MemberRepository;
import com.example.bcsd.dto.MemberRequestDto;
import com.example.bcsd.exception.DuplicateResourceException;
import com.example.bcsd.exception.InvalidRequestException;
import com.example.bcsd.exception.ResourceNotFoundException;
import com.example.bcsd.model.Article;
import com.example.bcsd.model.Member;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    public MemberService(MemberRepository memberRepository, ArticleRepository articleRepository) {
        this.memberRepository = memberRepository;
        this.articleRepository = articleRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 사용자입니다."));
    }

    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public Member createMember(MemberRequestDto memberDto) {
        Member member = new Member();
        member.setName(memberDto.getName());
        member.setEmail(memberDto.getEmail());
        member.setPassword(memberDto.getPassword());

        Optional<Member> existingMember = memberRepository.findByEmail(member.getEmail());
        if (existingMember.isPresent() && (member.getId() == null || !existingMember.get().getId().equals(member.getId()))) {
            throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
        }
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateMember(Long id, MemberRequestDto memberDto) {
        Member existingMemberEntity = getMemberById(id);
        
        if (!existingMemberEntity.getEmail().equals(memberDto.getEmail())) {
            Optional<Member> duplicateEmailMember = memberRepository.findByEmail(memberDto.getEmail());
            if (duplicateEmailMember.isPresent()) {
                throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
            }
        }
        
        existingMemberEntity.setName(memberDto.getName());
        existingMemberEntity.setEmail(memberDto.getEmail());
        existingMemberEntity.setPassword(memberDto.getPassword());
        
        return memberRepository.save(existingMemberEntity);
    }

    @Transactional
    public void deleteMemberById(Long id) {
        Member member = getMemberById(id);
        
        List<Article> memberArticles = articleRepository.findByMemberId(id);
        if (!memberArticles.isEmpty()) {
            throw new InvalidRequestException("사용자가 작성한 게시물이 존재하여 삭제할 수 없습니다.");
        }
        
        memberRepository.deleteById(id);
    }
} 