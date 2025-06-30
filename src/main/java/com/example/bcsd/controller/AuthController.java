package com.example.bcsd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bcsd.dto.MemberRequestDto;
import com.example.bcsd.model.Member;
import com.example.bcsd.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("member") != null) {
            return "redirect:/main";
        }
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String email, 
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        try {
            Member member = authService.login(email, password);
            session.setAttribute("member", member);
            return "redirect:/main";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
    
    @GetMapping("/signup")
    public String signupPage(HttpSession session, Model model) {
        if (session.getAttribute("member") != null) {
            return "redirect:/main";
        }
        model.addAttribute("memberRequestDto", new MemberRequestDto());
        return "signup";
    }
    
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute MemberRequestDto memberRequestDto,
                        BindingResult bindingResult,
                        Model model) {
        try {
            if (bindingResult.hasErrors()) {
                return "signup";
            }
            
            if (!memberRequestDto.getPassword().equals(memberRequestDto.getConfirmPassword())) {
                model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
                return "signup";
            }
            
            authService.signup(memberRequestDto);
            model.addAttribute("success", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }
    
    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/login";
        }
        model.addAttribute("member", member);
        return "main";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
} 