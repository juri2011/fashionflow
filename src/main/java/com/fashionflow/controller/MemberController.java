package com.fashionflow.controller;

import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    //로그인 페이지 이동
    @GetMapping("/members/login")
    public String loginPage(){
        return "members/memberLoginForm";
    }
    
    //회원가입 페이지 이동
    @GetMapping("members/register")
    public String registerPage() {
        return "members/memberRegister";
    }

    //회원 정보 입력
    @PostMapping("members/register")
    public String register(MemberFormDTO memberFormDTO){
        memberService.register(memberFormDTO);

        return "redirect:/";
    }
    
}
