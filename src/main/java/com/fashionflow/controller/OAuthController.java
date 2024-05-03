package com.fashionflow.controller;

import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.repository.MemberRepository;
import com.fashionflow.service.MemberService;
import com.fashionflow.service.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final MemberRepository memberRepository;
    private final OAuthService oAuthService;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;



    //OAuth 로그인
    @GetMapping("/login")
    public String oauthLogin(Model model, HttpServletRequest request) {
        // 컨트롤러 실행 시 MemberFormDTO 초기화 및 모델에 추가
        MemberFormDTO memberFormDTO = new MemberFormDTO();
        model.addAttribute("memberFormDTO", memberFormDTO);

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자가 OAuth 2.0 사용자인지 확인
        if (authentication.getPrincipal() instanceof OAuth2User) {

            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            // 사용자 정보에서 이메일 속성 추출
            String email = oauthUser.getAttribute("email");
            if (email != null) {
                memberFormDTO.setEmail(email);
            }
        }

        // 인증된 사용자가 OAuth 2.0 사용자인지 확인
        if (authentication.getPrincipal() instanceof OAuth2User) {

            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            // 사용자 정보에서 이메일 속성 추출
            String email = oauthUser.getAttribute("email");
            if (email != null) {
                memberFormDTO.setEmail(email);

                if(memberRepository.findByEmail(email) != null){
                    return "redirect:/";
                }
            }
        }

        return "oauth/oauthLogin";
    }


    //OAuth 회원 정보 등록
    @PostMapping("/register")
    public ModelAndView oauthRegister(MemberFormDTO memberFormDTO, BindingResult bindingResult, HttpServletRequest request){

        ModelAndView modelAndView = new ModelAndView();

        // 난수 비밀번호 생성
        String randomPwd = oAuthService.generateRandomString(20);

        memberFormDTO.setPwd(randomPwd);
        memberFormDTO.setConfirmPwd(randomPwd);

        //pwd 세팅 이후 유효성검사
        validator.validate(memberFormDTO, bindingResult);

        // 유효성 검사 실패 시 회원가입 페이지로 다시 이동
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            // 실패한 경우 ModelAndView에 기존에 입력한 정보 추가하여 전달
            modelAndView.addObject("memberFormDTO", memberFormDTO);
            modelAndView.setViewName("oauth/oauthLogin");
            return modelAndView;
        }

        try {
            // 현재 인증 객체 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 인증 객체가 OAuth2AuthenticationToken 인스턴스인지 확인
            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

                // provider와 providerId 설정
                String provider = oauthToken.getAuthorizedClientRegistrationId();
                String providerId = oauthToken.getName();

                memberFormDTO.setProvider(provider);
                memberFormDTO.setProviderId(providerId);
            }


            // 유효성 검사 성공 시 회원 등록 처리
            oAuthService.registerOAuth(memberFormDTO, passwordEncoder);

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