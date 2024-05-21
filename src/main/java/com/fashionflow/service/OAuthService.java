package com.fashionflow.service;

import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.ProfileImage;
import com.fashionflow.repository.MemberRepository;
import com.fashionflow.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ProfileImgService profileImgService;
    private final ProfileImageRepository profileImageRepository;

    //회원 등록 메소드
    public void registerOAuth(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) throws Exception {



        //Bcrypt 인코드
        String encodedPassword = passwordEncoder.encode(memberFormDTO.getPwd());

        Member member = Member.builder()
                .name(memberFormDTO.getName())
                .email(memberFormDTO.getEmail())
                .pwd(encodedPassword)
                .nickname(memberFormDTO.getNickname())
                .phone(memberFormDTO.getPhone())
                .birth(memberFormDTO.getBirth())
                .gender(memberFormDTO.getGender())
                .userAddr(memberFormDTO.getUserAddr())
                .userDaddr(memberFormDTO.getUserDaddr())
                .userStnum(memberFormDTO.getUserStnum())
                .regdate(LocalDateTime.now())
                .provider(memberFormDTO.getProvider())
                .providerId(memberFormDTO.getProviderId())
                .build();

        //중복 확인
        memberService.checkDuplicate(member);

        // 회원 저장
        memberRepository.save(member);

        // 프로필 이미지 저장 및 회원에 연결
        if (memberFormDTO.getProfileImageFile() != null) {
            ProfileImage profileImage = new ProfileImage();
            profileImgService.saveProfileImage(profileImage, memberFormDTO.getProfileImageFile());
            profileImage.setMember(member);
            member.setProfileImage(profileImage); // 연관 관계 설정

            // 프로필 이미지 저장
            profileImageRepository.save(profileImage);
        }

    }

    //난수 문자열 생성
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        Random rand = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = rand.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }




}
