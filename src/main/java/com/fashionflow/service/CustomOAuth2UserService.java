package com.fashionflow.service;

import com.fashionflow.entity.Member;
import com.fashionflow.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws UsernameNotFoundException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String providerId = userRequest.getClientRegistration().getRegistrationId(); // "google" 또는 "kakao"
        String email = null;

        if ("google".equals(providerId)) {
            email = oAuth2User.getAttribute("email");
        } else if ("kakao".equals(providerId)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            if (kakaoAccount != null) email = (String) kakaoAccount.get("email");
        } else if ("naver".equals(providerId)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("response");
            if (kakaoAccount != null) email = (String) kakaoAccount.get("email");
        }

        if (email == null) {
            throw new UsernameNotFoundException("이메일을 찾을 수 없습니다.");
        }

        Member member = memberRepository.findByEmail(email);
        if (member != null && !providerId.equals(member.getProvider())) {
            throw new UsernameNotFoundException("이미 다른 방법으로 등록된 이메일입니다.");
        }

        // 사용자 정보를 바탕으로 필요한 처리 수행 (사용자 등록 등)
        return oAuth2User;
    }
}
