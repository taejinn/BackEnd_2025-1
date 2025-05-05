package com.example.bcsd;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/introduce")
    public String hello(@RequestParam String name, Model model) {
        model.addAttribute("name", name != "" ? name : "박태진");
        return "hello";
    }

    @GetMapping("/json")
    @ResponseBody
    public User jsonPage() {
        User user = new User();
        user.setName("배진호");
        user.setAge(20); // ^_^
        return user;
    }
}

