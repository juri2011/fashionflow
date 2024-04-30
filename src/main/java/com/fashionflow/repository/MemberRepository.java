package com.fashionflow.repository;

import com.fashionflow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //이메일 체크
    Member findByEmail(String email);

    //회원가입 중복 데이터 체크
    Member findByEmailOrPhoneOrNickname(String email, String phone, String nickname);

    //닉네임 찾기
    Member findByNickname(String nickname);

    //아이디 찾기
    Member findByNameAndPhone(String name, String phone);

}
