package com.fashionflow.repository;

import com.fashionflow.entity.MemberAuthenticationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberAuthenticationCodeRepository extends JpaRepository<MemberAuthenticationCode, Long> {

    // 이메일로 end_date가 지금 이후고, delete_date가 null인 데이터 찾아오기
    Optional<MemberAuthenticationCode> findByEmailAndEndDateAfterAndDeleteDateIsNull(String email, LocalDateTime currentDateTime);

}
