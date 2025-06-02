package com.example.bcsd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.dao.ArticleDao;
import com.example.bcsd.dao.MemberDao;
import com.example.bcsd.dto.MemberRequestDto;
import com.example.bcsd.exception.DuplicateResourceException;
import com.example.bcsd.exception.InvalidRequestException;
import com.example.bcsd.exception.ResourceNotFoundException;
import com.example.bcsd.model.Article;
import com.example.bcsd.model.Member;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final ArticleDao articleDao;

    public MemberService(MemberDao memberDao, ArticleDao articleDao) {
        this.memberDao = memberDao;
        this.articleDao = articleDao;
    }

    public List<Member> getAllMembers() {
        return memberDao.findAll();
    }

    public Member getMemberById(Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 사용자입니다."));
    }

    public Optional<Member> getMemberByEmail(String email) {
        return memberDao.findByEmail(email);
    }

    @Transactional
    public Member createMember(MemberRequestDto memberDto) {
        Member member = new Member();
        member.setName(memberDto.getName());
        member.setEmail(memberDto.getEmail());
        member.setPassword(memberDto.getPassword());

        Optional<Member> existingMember = memberDao.findByEmail(member.getEmail());
        if (existingMember.isPresent() && (member.getId() == null || !existingMember.get().getId().equals(member.getId()))) {
            throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
        }
        return memberDao.save(member);
    }

    @Transactional
    public Member updateMember(Long id, MemberRequestDto memberDto) {
        Member existingMemberEntity = getMemberById(id);
        
        if (!existingMemberEntity.getEmail().equals(memberDto.getEmail())) {
            Optional<Member> duplicateEmailMember = memberDao.findByEmail(memberDto.getEmail());
            if (duplicateEmailMember.isPresent()) {
                throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
            }
        }
        
        existingMemberEntity.setName(memberDto.getName());
        existingMemberEntity.setEmail(memberDto.getEmail());
        existingMemberEntity.setPassword(memberDto.getPassword());
        
        return memberDao.save(existingMemberEntity);
    }

    @Transactional
    public void deleteMemberById(Long id) {
        Member member = getMemberById(id);
        
        List<Article> memberArticles = articleDao.findByMemberId(id);
        if (!memberArticles.isEmpty()) {
            throw new InvalidRequestException("사용자가 작성한 게시물이 존재하여 삭제할 수 없습니다.");
        }
        
        memberDao.deleteById(id);
    }
} 