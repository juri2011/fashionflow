package com.fashionflow.repository;

import com.fashionflow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //이메일 체크
    Member findByEmail(String email);

    //회원가입 중복 데이터 체크
    Member findByEmailOrPhoneOrNickname(String email, String phone, String nickname);

}
