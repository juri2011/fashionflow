package com.fashionflow.controller;

import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    /* 회원정보 */
    private final MemberService memberService;

    @GetMapping("/chat")
    public String chatGET(Principal principal, Model model){

        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }

        System.out.println("@ChatController, chat GET()");

        //회원 정보가 없으면 '익명' 처리 -> 그러나 채팅방은 기본적으로 회원 전용이므로 에러메시지와 동일...
        /*String userName = "anonymous";
        if(principal != null){
            Member member = memberService.findMemberByCurrentEmail();
            userName = member.getNickname();
            //프사 있으면 좋을듯
        }
        model.addAttribute("userName", userName);*/
        return "chat/chater";
    }

    @GetMapping("/getUsername")
    public @ResponseBody ResponseEntity getUsername(){

        try{

            Member member = memberService.findMemberByCurrentEmail();
            String username = member.getNickname();
            return new ResponseEntity<String>(username, HttpStatus.OK);
        }catch(NullPointerException e){
            return new ResponseEntity<String>("로그인 후 이용해주세요", HttpStatus.UNAUTHORIZED);
        }
    }
}