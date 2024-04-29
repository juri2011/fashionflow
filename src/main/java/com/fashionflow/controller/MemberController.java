package com.fashionflow.controller;

import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
/*import com.fashionflow.service.MemberService;*/
import com.fashionflow.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
/*
import org.springframework.security.crypto.password.PasswordEncoder;
*/
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequiredArgsConstructor
public class MemberController {


    private final MemberService memberService;


    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입



    //로그인 페이지 이동
    @GetMapping("/members/login")
    public String loginPage(){
        return "members/memberLoginForm";
    }

    @GetMapping("/members/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 비밀번호를 확인해주세요");
        return "/members/memberLoginForm";
    }
    
    //회원가입 페이지 이동
    @GetMapping("members/register")
    public String registerPage(Model model) {
        model.addAttribute("memberFormDTO", new MemberFormDTO());
        return "members/memberRegister";
    }

    // 회원 정보 입력
    @PostMapping("/members/register")
    public ModelAndView registerMember(@Valid MemberFormDTO memberFormDTO, BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView();

        // 비밀번호와 비밀번호 확인이 일치하는지 확인
        if (!memberFormDTO.getPwd().equals(memberFormDTO.getConfirmPwd())) {
            //에러 메세지 바인딩 시키는 메소드
            bindingResult.rejectValue("confirmPwd", "error.memberFormDTO", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 유효성 검사 실패 시 회원가입 페이지로 다시 이동
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            // 실패한 경우 ModelAndView에 기존에 입력한 정보 추가하여 전달
            modelAndView.addObject("memberFormDTO", memberFormDTO);
            modelAndView.setViewName("members/memberRegister");
            return modelAndView;
        }

        try {

            // 유효성 검사 성공 시 회원 등록 처리

            memberService.registerMember(memberFormDTO, passwordEncoder);


            // 회원가입 성공 시 메인 페이지로 리다이렉트
            modelAndView.setViewName("redirect:/");
        } catch (IllegalStateException e) {
            // 실패한 경우 ModelAndView에 기존에 입력한 정보 추가하여 전달
            modelAndView.addObject("memberFormDTO", memberFormDTO);
            // 중복 회원 예외 처리
            modelAndView.addObject("error", e.getMessage());
            modelAndView.setViewName("members/memberRegister");
        }

        return modelAndView;
    }



}
