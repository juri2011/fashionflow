package com.fashionflow.repository;

import com.fashionflow.entity.ReportItem;
import com.fashionflow.entity.ReportMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 회원 신고에 대한 데이터 액세스를 처리하는 리포지토리
public interface ReportMemberRepository extends JpaRepository<ReportMember, Long> {

    // 등록 날짜를 기준으로 모든 회원 신고를 내림차순으로 가져오는 메서드
    public List<ReportMember> findAllByOrderByRegdateDesc();

    // ID를 기준으로 모든 회원 신고를 페이지로 내림차순으로 가져오는 메서드
    public Page<ReportMember> findAllByOrderByIdDesc(Pageable pageable);
}