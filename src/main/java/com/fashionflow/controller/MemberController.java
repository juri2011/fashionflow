package com.fashionflow.controller;

import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class MemberController {

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


    
}
