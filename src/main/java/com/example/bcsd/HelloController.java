package com.example.bcsd;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.bcsd.model.Member;

@Controller
public class HelloController {

    @GetMapping("/introduce")
    public String hello(@RequestParam(defaultValue = "박태진") String name, Model model) {
        model.addAttribute("name", name != "" ? name : "박태진");
        return "hello";
    }

    @GetMapping("/json")
    @ResponseBody
    public Member jsonPage() {
        Member member = new Member();
        member.setName("배진호");
        member.setEmail("test@example.com");
        member.setId(1L);
        return member;
    }
}