package com.fashionflow.controller;

import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입



    //로그인 페이지 이동
    @GetMapping("/login")
    public String loginPage(){
        return "members/memberLoginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 비밀번호를 확인해주세요");
        return "members/memberLoginForm";
    }

    //회원가입 페이지 이동
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("memberFormDTO", new MemberFormDTO());
        return "members/memberRegister";
    }

    // 회원 정보 입력
    @PostMapping("/register")
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

    //회원 수정 페이지
    @GetMapping("/memberEdit")
    public String memberEditPage(Model model) {
        Member currentMember = memberService.findMemberByCurrentEmail();

        model.addAttribute("currentMember", currentMember);

        return "members/memberEdit"; // 수정 페이지로 이동

    }

    //회원 수정
    @PostMapping("/memberEdit")
    public String memberEdit(@Valid MemberFormDTO memberFormDTO, BindingResult bindingResult, Model model) {

        // 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            // 현재 멤버 정보를 가져와서 다시 모델에 추가
            Member currentMember = memberService.findMemberByCurrentEmail();
            model.addAttribute("currentMember", currentMember);
            // 유효성 검사에 실패한 필드에 대한 오류 메시지를 추출하여 모델에 추가
            model.addAttribute("errors", bindingResult.getAllErrors());
            // 유효성 검사 실패에 따른 오류 메시지를 뷰로 전달
            return "members/memberEdit"; // 에러가 발생한 페이지로 리디렉션 또는 해당 페이지로 포워딩
        }

        // 비밀번호와 비밀번호 확인 일치 여부 확인
        if (!memberFormDTO.getPwd().equals(memberFormDTO.getConfirmPwd())) {
            model.addAttribute("pwdErrorMessage", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            // 현재 멤버 정보를 가져와서 다시 모델에 추가
            Member currentMember = memberService.findMemberByCurrentEmail();
            model.addAttribute("currentMember", currentMember);
            return "members/memberEdit"; // 에러가 발생한 페이지로 리디렉션 또는 해당 페이지로 포워딩
        }

        // 닉네임 중복 검사
        try {
            memberService.updateMember(memberFormDTO, passwordEncoder);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            // 현재 멤버 정보를 가져와서 다시 모델에 추가
            Member currentMember = memberService.findMemberByCurrentEmail();
            model.addAttribute("currentMember", currentMember);

            model.addAttribute("duplicateErrorMessage", e.getMessage());
            return "members/memberEdit"; // 에러가 발생한 페이지로 리디렉션 또는 해당 페이지로 포워딩
        }

    }

    // 회원 탈퇴
    @PostMapping("/deleteMember")
    public ResponseEntity<String> deleteMember() {
        String email = memberService.currentMemberEmail();
        if (email == null) {
            return new ResponseEntity<>("현재 로그인된 사용자를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
        }

        try {
            memberService.deleteMember(email);
            return new ResponseEntity<>("회원 삭제가 완료되었습니다.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("회원 삭제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //아이디 찾기 페이지
    @GetMapping("/findId")
    public String findIdPage(){
        return "/members/findId";
    }

    //회원 아이디 찾기
    @PostMapping("/findId")
    public String findIdByNameAndPhone(@RequestParam("name") String name, @RequestParam("phone") String phone, Model model) {

        try {
            String email = memberService.findId(name, phone);
            model.addAttribute("email", email);
            return "members/findId";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/members/findId";
        }
    }
}
