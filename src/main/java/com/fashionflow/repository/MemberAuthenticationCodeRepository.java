package com.fashionflow.repository;

import com.fashionflow.entity.MemberAuthenticationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

// 회원 인증 코드에 대한 데이터 액세스를 처리하는 리포지토리
public interface MemberAuthenticationCodeRepository extends JpaRepository<MemberAuthenticationCode, Long> {

    // 주어진 이메일로, 현재 시간 이후이고 삭제 날짜가 null인 데이터를 찾아오는 메서드
    Optional<MemberAuthenticationCode> findByEmailAndEndDateAfterAndDeleteDateIsNull(String email, LocalDateTime currentDateTime);

}
