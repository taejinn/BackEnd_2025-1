package com.example.bcsd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bcsd.model.Member;
import com.example.bcsd.service.MemberService;
import com.example.bcsd.dto.MemberRequestDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }
    
    @GetMapping("/member/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }
    
    @PostMapping("/member")
    public ResponseEntity<Member> createMember(@Valid @RequestBody MemberRequestDto memberDto) {
        Member savedMember = memberService.createMember(memberDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }
    
    @PutMapping("/member/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @Valid @RequestBody MemberRequestDto memberDto) {
        Member updatedMember = memberService.updateMember(id, memberDto);
        return ResponseEntity.ok(updatedMember);
    }
    
    @DeleteMapping("/member/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMemberById(id);
        return ResponseEntity.noContent().build();
    }
} 