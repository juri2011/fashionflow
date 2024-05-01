package com.fashionflow.repository;

import com.fashionflow.entity.ReportMember;
import com.fashionflow.entity.ReportMemberTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportMemberTagRepository extends JpaRepository<ReportMemberTag, Long> {

    public List<ReportMemberTag> findAllByReportMember_Id(Long reportMemberId);
}
