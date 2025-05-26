package com.example.bcsd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.dao.ArticleDao;
import com.example.bcsd.dao.MemberDao;
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

    public List<Member> findAllMembers() {
        return memberDao.findAll();
    }

    public Member findMemberById(Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 사용자입니다."));
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberDao.findByEmail(email);
    }

    @Transactional
    public Member saveMember(Member member) {
        validateMemberFields(member);
        
        Optional<Member> existingMember = memberDao.findByEmail(member.getEmail());
        if (existingMember.isPresent() && !existingMember.get().getId().equals(member.getId())) {
            throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
        }
        return memberDao.save(member);
    }

    @Transactional
    public Member updateMember(Long id, Member updatedMember) {
        Member existingMember = findMemberById(id);
        
        if (!existingMember.getEmail().equals(updatedMember.getEmail())) {
            Optional<Member> duplicateEmailMember = memberDao.findByEmail(updatedMember.getEmail());
            if (duplicateEmailMember.isPresent()) {
                throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
            }
        }
        
        validateMemberFields(updatedMember);
        
        existingMember.setName(updatedMember.getName());
        existingMember.setEmail(updatedMember.getEmail());
        existingMember.setPassword(updatedMember.getPassword());
        
        return memberDao.save(existingMember);
    }

    @Transactional
    public void deleteMemberById(Long id) {
        Member member = findMemberById(id);
        
        List<Article> memberArticles = articleDao.findByMemberId(id);
        if (!memberArticles.isEmpty()) {
            throw new InvalidRequestException("사용자가 작성한 게시물이 존재하여 삭제할 수 없습니다.");
        }
        
        memberDao.deleteById(id);
    }

    private void validateMemberFields(Member member) {
        if (member.getName() == null || member.getEmail() == null || member.getPassword() == null) {
            throw new InvalidRequestException("사용자 정보의 필수 필드가 누락되었습니다.");
        }
    }
} 