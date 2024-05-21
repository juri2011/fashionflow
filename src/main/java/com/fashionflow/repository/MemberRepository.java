package com.fashionflow.repository;

import com.fashionflow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.Optional;

// 회원에 대한 데이터 액세스를 처리하는 리포지토리
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 주어진 이메일에 해당하는 회원을 찾는 메서드
    Member findByEmail(String email);

    // 주어진 이메일, 전화번호, 닉네임 중 하나와 일치하는 회원을 찾는 메서드
    Member findByEmailOrPhoneOrNickname(String email, String phone, String nickname);

    // 주어진 닉네임에 해당하는 회원을 찾는 메서드
    Member findByNickname(String nickname);

    // 주어진 이름과 전화번호에 해당하는 회원을 찾는 메서드
    Member findByNameAndPhone(String name, String phone);

}