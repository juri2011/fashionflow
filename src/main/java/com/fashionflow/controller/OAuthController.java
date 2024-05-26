package com.fashionflow.controller;

import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.ProfileImage;
import com.fashionflow.repository.MemberRepository;
import com.fashionflow.repository.ProfileImageRepository;
import com.fashionflow.service.MemberService;
import com.fashionflow.service.OAuthService;
import com.fashionflow.service.ProfileImgService;
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
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final OAuthService oAuthService;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;
    private final ProfileImageRepository profileImageRepository;
    private final ProfileImgService profileImgService;



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
            String email = null;

            // 카카오 계정인 경우
            if (oauthUser.getAttributes().containsKey("kakao_account")) {
                Map<String, Object> kakaoAccount = oauthUser.getAttribute("kakao_account");
                //email 키값 return 후, Map 형식 형변환
                email = (String) kakaoAccount.get("email");
            }
            //네이버 계정인 경우
            else if (oauthUser.getAttributes().containsKey("response")) {
                Map<String, Object> naverAccount = oauthUser.getAttribute("response");
                //email 키값 return 후, Map 형식 형변환
                email = (String) naverAccount.get("email");
            }
            //구글 계정인 경우
            else {
                email = oauthUser.getAttribute("email");
            }

            if (email != null) {
                memberFormDTO.setEmail(email);

                // 해당 이메일이 이미 등록되어 있는지 확인
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
            modelAndView.setViewName("oauth/oauthLogin");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return modelAndView;
    }

    @PostMapping("/oauthEdit")
    public String memberEdit(MemberFormDTO memberFormDTO, BindingResult bindingResult, Model model) {

        // 난수 비밀번호 생성
        String randomPwd = oAuthService.generateRandomString(20);

        memberFormDTO.setPwd(randomPwd);
        memberFormDTO.setConfirmPwd(randomPwd);

        //pwd 세팅 이후 유효성검사
        validator.validate(memberFormDTO, bindingResult);

        // 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            // 현재 멤버 정보를 가져와서 다시 모델에 추가
            Member currentMember = memberService.findMemberByCurrentEmail();
            // 프로필 이미지 정보를 가져옴
            ProfileImage profileImage = profileImageRepository.findByMemberId(currentMember.getId());

            model.addAttribute("currentMember", currentMember);
            model.addAttribute("profileImage", profileImage);  // 프로필 이미지 추가
            // 유효성 검사에 실패한 필드에 대한 오류 메시지를 추출하여 모델에 추가
            model.addAttribute("errors", bindingResult.getAllErrors());
            // 유효성 검사 실패에 따른 오류 메시지를 뷰로 전달
            return "oauth/oauthEdit"; // 에러가 발생한 페이지로 리디렉션 또는 해당 페이지로 포워딩
        }

        // 닉네임 중복 검사
        try {
            boolean deleteImage = (memberFormDTO.getProfileImageFile() != null && memberFormDTO.getProfileImageFile().isEmpty());
            memberService.updateMember(memberFormDTO, passwordEncoder, deleteImage);

            // 프로필 이미지 삭제
            Member currentMember = memberService.findMemberByCurrentEmail();
            ProfileImage profileImage = currentMember.getProfileImage();
            if (profileImage != null) {
                profileImgService.deleteProfileImage(profileImage);
            }

            return "redirect:/";
        } catch (Exception e) {
            // 현재 멤버 정보를 가져와서 다시 모델에 추가
            Member currentMember = memberService.findMemberByCurrentEmail();
            // 프로필 이미지 정보를 가져옴
            ProfileImage profileImage = profileImageRepository.findByMemberId(currentMember.getId());

            model.addAttribute("currentMember", currentMember);
            model.addAttribute("profileImage", profileImage);  // 프로필 이미지 추가

            model.addAttribute("duplicateErrorMessage", e.getMessage());
            return "oauth/oauthEdit"; // 에러가 발생한 페이지로 리디렉션 또는 해당 페이지로 포워딩
        }
    }



}
