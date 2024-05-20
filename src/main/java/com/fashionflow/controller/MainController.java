package com.fashionflow.controller;

import com.fashionflow.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.fashionflow.dto.BuyerDTO;
import com.fashionflow.dto.ListingItemDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/")
    public String main(Model model){

        if(memberService.currentMemberEmail()!= null && memberService.findMemberByCurrentEmail() == null){
            return "redirect:/oauth/login";
        }

        List<ListingItemDTO> Top8products = itemService.getTop8productswithImg();

        model.addAttribute("Top8products", Top8products); // 리뷰가 이미 작성되었는지 여부를 모델에 추가

        return "main";

    }

    @GetMapping("/EmailVerify")
    public String emailVerify(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자가 OAuth 2.0 사용자인지 확인
        if ((authentication.getPrincipal() instanceof OAuth2User)) {
            // OAuth로 로그인한 경우, 오류 메시지를 표시하고 페이지 이동을 막음
            model.addAttribute("errorMessage", "간편 로그인 회원은 사용할 수 없는 서비스입니다.");
            return "/error/errorPage";
        }

        // OAuth로 로그인한 사용자 아닌 경우에만 EmailVerify 페이지로 이동
        return "/EmailVerify";
    }

    @GetMapping("/ResetPwd")
    public String resetPassword() {
        return "ResetPwd";
    }

    @GetMapping("/error/errorPage")
    public String errorPage() {
        return "errorPage";
    }

    @GetMapping("/error/loginError")
    public String loginError() {
        return "loginError";
    }

    @GetMapping("/chart") // 과목별 점수 합계 + 평균  // 두 종류의 그래프
    public String allItems(Model model) {
        itemService.addallItems(model);
        return "chart";
    }

    

}