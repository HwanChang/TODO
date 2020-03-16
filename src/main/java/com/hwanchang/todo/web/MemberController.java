package com.hwanchang.todo.web;

import com.hwanchang.todo.dto.member.MemberSaveRequestDto;
import com.hwanchang.todo.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class MemberController {

    private MemberService memberService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signup")
    public String signup(Model model, HttpServletRequest req){
        model.addAttribute("message",req.getServletContext());
        return "signup";
    }

    @GetMapping("/signin")
    public String signin(Model model, HttpServletRequest req){
        model.addAttribute("message",req.getServletContext());
        return "signin";
    }

    @PostMapping("/signupprocess")
    public String signupprocess(MemberSaveRequestDto memberSaveRequestDto){
        memberService.joinUser(memberSaveRequestDto);
        return "redirect:/signin";
    }

    @PostMapping("/signinprocess")
    public String signinprocess(MemberSaveRequestDto memberSaveRequestDto){
        memberService.loadUserByUsername(memberSaveRequestDto.getEmail());
        return "redirect:/";
    }
}