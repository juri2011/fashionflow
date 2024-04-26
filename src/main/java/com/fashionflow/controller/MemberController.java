package com.fashionflow.controller;

import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


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
    // 회원 정보 입력
    @PostMapping("/members/register")
    public ModelAndView registerMember(@Valid MemberFormDTO memberFormDTO, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        // 유효성 검사 실패 시 회원가입 페이지로 다시 이동
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.setViewName("members/memberRegister");
            return modelAndView;
        }

        try {
            // 유효성 검사 성공 시 회원 등록 처리
            memberService.registerMember(memberFormDTO);

            // 회원가입 성공 시 메인 페이지로 리다이렉트
            modelAndView.setViewName("redirect:/");
        } catch (IllegalStateException e) {
            // 중복 회원 예외 처리
            modelAndView.addObject("error", e.getMessage());
            modelAndView.setViewName("members/memberRegister");
        }

        return modelAndView;
    }



}
