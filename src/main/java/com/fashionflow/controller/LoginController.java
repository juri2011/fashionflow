package com.fashionflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    //로그인 페이지 이동
    @GetMapping("/members/login")
    public String login(){
        return "members/memberLoginForm";
    }
    
    //회원가입 페이지 이동
    @GetMapping("members/register")
    public String register() {
        return "members/memberRegister";
    }
    
}
