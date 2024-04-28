package com.fashionflow.service;

import com.fashionflow.constant.Gender;
import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //회원 등록 메소드
    public void registerMember(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) {



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
                .build();

        //중복 확인
        checkDuplicate(member);

        memberRepository.save(member);

    }

    //중복 체크 메소드
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


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPwd())
                .roles(member.getRole().toString())
                .build();
    }

    //현재 로그인된 사용자 security를 이용해 email 반환
    public String currentMemberEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // 사용자의 이메일 반환
        }
        return null;
    }

    // 반환된 email로 사용자의 정보 조회 메소드
    public Member findMemberByCurrentEmail() {
        String email = currentMemberEmail();
        if (email != null) {
            return memberRepository.findByEmail(email);
        }
        return null;
    }


    // 회원 정보 업데이트 메서드
    public void updateMember(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) {

        //Bcrypt 인코드
        String encodedPassword = passwordEncoder.encode(memberFormDTO.getPwd());

        // 이메일로 사용자 조회
        Member currentMember = memberRepository.findByEmail(memberFormDTO.getEmail());

        Member exsitingMember = memberRepository.findByNickname(memberFormDTO.getNickname());

        if(exsitingMember != null && !exsitingMember.getId().equals(currentMember.getId())){

            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }


        // 회원 정보 업데이트
        currentMember.setPwd(encodedPassword);
        currentMember.setNickname(memberFormDTO.getNickname());
        currentMember.setGender(memberFormDTO.getGender());
        currentMember.setUserStnum(memberFormDTO.getUserStnum());
        currentMember.setUserAddr(memberFormDTO.getUserAddr());
        currentMember.setUserDaddr(memberFormDTO.getUserDaddr());

        memberRepository.save(currentMember);
    }




}
