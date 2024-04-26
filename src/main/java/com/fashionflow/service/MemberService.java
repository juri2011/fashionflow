package com.fashionflow.service;

import com.fashionflow.constant.Gender;
import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원 등록 메소드
    @Transactional
    public void registerMember(MemberFormDTO memberFormDTO) {

        Member member = Member.builder()
                .name(memberFormDTO.getName())
                .email(memberFormDTO.getEmail())
                .pwd(memberFormDTO.getPwd())
                .nickname(memberFormDTO.getNickname())
                .phone(memberFormDTO.getPhone())
                .birth(memberFormDTO.getBirth())
                .gender(memberFormDTO.getGender())
                .userAddr(memberFormDTO.getUserAddr())
                .userDaddr(memberFormDTO.getUserDaddr())
                .userStnum(memberFormDTO.getUserStnum())
                .regdate(LocalDateTime.now())
                .build();

        checkDuplicate(member);

        memberRepository.save(member);

    }

    private void checkDuplicate(Member member) {
        Member checkMember = memberRepository.findByEmailOrPhoneOrNickname(member.getEmail(), member.getPhone(), member.getNickname());
        if (checkMember != null) {
            if (checkMember.getEmail().equals(member.getEmail())) {
                throw new IllegalStateException("이미 가입된 이메일입니다.");
            }
            if (checkMember.getPhone().equals(member.getPhone())) {
                throw new IllegalStateException("이미 가입된 휴대폰 번호입니다.");
            }
            if (checkMember.getNickname().equals(member.getNickname())) {
                throw new IllegalStateException("중복되는 닉네임입니다.");
            }
        }
    }
}