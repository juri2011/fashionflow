package com.fashionflow.repository;

import com.fashionflow.entity.ReportMember;
import com.fashionflow.entity.ReportMemberTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 회원 신고 태그에 대한 데이터 액세스를 처리하는 리포지토리
public interface ReportMemberTagRepository extends JpaRepository<ReportMemberTag, Long> {

    // 주어진 회원 신고 ID에 해당하는 모든 회원 신고 태그를 가져오는 메서드
    public List<ReportMemberTag> findAllByReportMember_Id(Long reportMemberId);
}