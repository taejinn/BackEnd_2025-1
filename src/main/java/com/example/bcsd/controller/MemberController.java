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

@RestController
public class MemberController {
    
    private final MemberService memberService;
    
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    
    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();
        return ResponseEntity.ok(members);
    }
    
    @GetMapping("/member/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        Member member = memberService.findMemberById(id);
        return ResponseEntity.ok(member);
    }
    
    @PostMapping("/member")
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        Member savedMember = memberService.saveMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }
    
    @PutMapping("/member/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        Member updatedMember = memberService.updateMember(id, member);
        return ResponseEntity.ok(updatedMember);
    }
    
    @DeleteMapping("/member/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMemberById(id);
        return ResponseEntity.noContent().build();
    }
} 