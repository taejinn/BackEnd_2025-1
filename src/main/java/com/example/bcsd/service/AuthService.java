package com.example.bcsd.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bcsd.dto.MemberRequestDto;
import com.example.bcsd.exception.InvalidRequestException;
import com.example.bcsd.exception.ResourceNotFoundException;
import com.example.bcsd.model.Member;

@Service
public class AuthService {
    
    private final MemberService memberService;
    
    public AuthService(MemberService memberService) {
        this.memberService = memberService;
    }
    
    public Member login(String email, String password) {
        Optional<Member> memberOpt = memberService.getMemberByEmail(email);
        
        if (memberOpt.isEmpty()) {
            throw new ResourceNotFoundException("존재하지 않는 이메일입니다.");
        }
        
        Member member = memberOpt.get();
        if (!member.getPassword().equals(password)) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }
        
        return member;
    }
    
    public Member signup(MemberRequestDto memberDto) {
        return memberService.createMember(memberDto);
    }
} 