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

        memberRepository.save(member);
    }

    private void check(MemberFormDTO memberFormDTO){

    }
}